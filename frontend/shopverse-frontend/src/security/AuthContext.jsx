import { createContext, useContext, useEffect, useState } from "react";
import { apiClient } from "../services/ApiClient";
import { executeJwtAuthenticationServiceApi } from "../services/AuthenticationApiService";
import { getCartApi } from "../services/ApiService";

export const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export default function AuthProvider({ children }) {

    const [isAuthenticated, setAuthenticated] = useState(false);
    const [email, setEmail] = useState(null);
    const [token, setToken] = useState(null)
    const [cartCount, setCartCount] = useState(0);
    const [user, setUser] = useState(null);

    useEffect(() => {
        const storedToken = localStorage.getItem("token");
        const storedUser = localStorage.getItem("user");

        if (storedToken && storedUser) {
            const parsedUser = JSON.parse(storedUser);
            setToken(storedToken);
            setUser(parsedUser);
            setEmail(parsedUser.email);
            setAuthenticated(true);

            apiClient.interceptors.request.use((config) => {
                config.headers.Authorization = storedToken;
                return config;
            });
        }
    }, []);

    useEffect(() => {
        if (isAuthenticated && user && token) {
            loadCartCount();
        }
    }, [isAuthenticated, user, token]);

    async function login(email, password) {
        try {
            const response = await executeJwtAuthenticationServiceApi(email, password);

            if (response.status === 200) {
                const jwtToken = "Bearer " + response.data.token;
                const loggedInUser = {
                    id: response.data.id,
                    email: response.data.email,
                    roles: response.data.roles
                };

                setAuthenticated(true);
                setEmail(response.data.email);
                setToken(jwtToken);
                setUser(loggedInUser);

                localStorage.setItem("token", jwtToken);
                localStorage.setItem("user", JSON.stringify(loggedInUser));

                apiClient.interceptors.request.use(
                    (config) => {
                        config.headers.Authorization = jwtToken;
                        return config;
                    });


                return loggedInUser;
            }
        } catch (error) {
            logout();
            return null;
        }
    }

    function logout() {
        setAuthenticated(false);
        setToken(null);
        setEmail(null);
        setUser(null);
        setCartCount(0);
        localStorage.removeItem("token");
        localStorage.removeItem("user");
    }

    const loadCartCount = async () => {

        if (!user || !user.id) {
            setCartCount(0);
            return;

        }
        try {
            const response = await getCartApi(user.id, token);
            const items = response.data.items || [];
            const total = items.reduce((sum, item) => sum + item.quantity, 0);
            setCartCount(total);
        } catch (error) {
            console.error("Failed to load cart", error);
            setCartCount(0);
        }
    };

    const isAdmin = () => {
        return user?.roles?.includes("ADMIN");
    };

    return (
        <AuthContext.Provider value={{ isAuthenticated, login, logout, user, email, token, cartCount, loadCartCount, isAdmin }}>
            {children}
        </AuthContext.Provider>
    );
}