import { Link, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import { useAuth } from "../security/AuthContext";
import { searchProductsApi } from "../services/ApiService";

export default function Navbar() {
    const [searchQuery, setSearchQuery] = useState("");
    const [results, setResults] = useState([]);
    const [showDropDown, setShowDropDown] = useState(false);

    const navigate = useNavigate();
    const authContext = useAuth();

    const handleSearch = (e) => {
        e.preventDefault();
        if (searchQuery.trim() !== "") {
            navigate(`/search?q=${encodeURIComponent(searchQuery)}`);
            setShowDropDown(false);
        }
    };

    const handleLogout = () => {
        authContext.logout();
        navigate("/");
    };

    useEffect(() => {
        if (!searchQuery.trim()) {
            setResults([]);
            return;
        }

        const delayDebounce = setTimeout(() => {
            searchProductsApi(searchQuery)
                .then((res) => {
                    setResults(res.data);
                    setShowDropDown(true);
                })
                .catch(() => setResults([]));
        }, 400);

        return () => clearTimeout(delayDebounce);
    }, [searchQuery]);

    return (
        <nav className="bg-white shadow-sm">
            <div className="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">

                {/* Logo */}
                <Link to="/" className="text-2xl font-bold text-blue-600">
                    ShopVerse
                </Link>

                {/* Navigation Links */}
                <ul className="flex space-x-6 items-center">
                    <li>
                        <Link to="/" className="text-gray-700 hover:text-blue-500 transition">
                            Home
                        </Link>
                    </li>
                    <li>
                        <Link to="/products" className="text-gray-700 hover:text-blue-500 transition">
                            Products
                        </Link>
                    </li>
                    <li>
                        <Link to="/cart" className="text-gray-700 hover:text-blue-500 transition">
                            Cart
                        </Link>
                    </li>
                    {authContext.isAuthenticated && (
                        <>
                            <li>
                                <Link to="/orders" className="text-gray-700 hover:text-blue-500 transition">
                                    Orders
                                </Link>
                            </li>
                            <li>
                                <Link to="/checkout" className="text-gray-700 hover:text-blue-500 transition">
                                    Checkout
                                </Link>
                            </li>
                        </>
                    )}
                </ul>

                <div>
                    {authContext.isAuthenticated && (
                        <Link to="/cart" className="relative text-gray-700 hover:text-blue-500 transition">
                            🛒{authContext.cartCount > 0 && (
                                <span className="absolute -top-2 -right-3 bg-red-500 text-white text-xs px-2 py-1 rounded-full">
                                    {authContext.cartCount}
                                </span>
                            )}
                        </Link>
                    )}
                </div>

                {/* Search Bar */}
                <div className="relative flex-grow max-w-md">
                    <form onSubmit={handleSearch} className="flex items-center space-x-2 flex-grow max-w-md mx-4">
                        <input
                            type="text"
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                            placeholder="Search products..."
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                            onFocus={() => searchQuery && setShowDropDown(true)}
                            onBlur={() => setTimeout(() => setShowDropDown(false), 200)}
                        />
                        <button
                            type="submit"
                            className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition"
                        >
                            Search
                        </button>
                    </form>

                    {showDropDown && results.length > 0 && (
                        <ul className="absolute left-0 right-0 bg-white border border-gray-200 rounded-lg mt-1 shadow-lg mx-h-60 overflow-auto z-50">
                            {results.map((product) => (
                                <li
                                    key={product.id}
                                    className="px-4 py-2 hover:bg-gray-100 cursor-pointer"
                                    onMouseDown={() => {
                                        navigate(`/products/${product.id}`);
                                        setShowDropDown(false);
                                    }}
                                >
                                    {product.title}
                                </li>
                            ))}
                        </ul>
                    )}
                </div>

                {/* Auth Buttons */}
                <ul className="flex space-x-4 items-center">
                    {!authContext.isAuthenticated && (
                        <>
                            <li>
                                <Link
                                    to="/login"
                                    className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition"
                                >
                                    Login
                                </Link>
                            </li>
                            <li>
                                <Link
                                    to="/register"
                                    className="bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600 transition"
                                >
                                    Register
                                </Link>
                            </li>
                        </>
                    )}
                    {authContext.isAuthenticated && (
                        <li>
                            <button
                                onClick={handleLogout}
                                className="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 transition"
                            >
                                Logout
                            </button>
                        </li>
                    )}
                    {authContext.isAuthenticated && authContext.isAdmin() && (
                        <li>
                            <Link to="/admin/products"
                                className="bg-purple-500 text-white px-4 py-2 rounded-lg hover:bg-purple-600  transition"
                            >
                                Admin
                            </Link>
                        </li>
                    )}
                </ul>
            </div>
        </nav>
    );
}
