import { useEffect, useState } from "react"
import { addToCartApi, allProductsApi } from "../services/ApiService";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { useAuth } from "../security/AuthContext";


export default function Products() {

    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [quantities, setQuantities] = useState({});
    const authContext = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        allProductsApi()
            .then((response) => {
                setProducts(response.data);

                const initialQuantities = {};
                response.data.forEach(p => initialQuantities[p.id] = 1);
                setQuantities(initialQuantities);
            })
            .catch((error) => {
                console.error("Error fetching products:", error);
                setError("Failed to load products");
            })
            .finally(() => {
                setLoading(false);
            });
    }, []);

    if (loading) {
        return <p className="text-center text-gray-500">Loading products...</p>;
    }

    if (error) {
        return <p className="text-center text-red-500">{error}</p>;
    }

    function viewProduct(id) {
        navigate(`/viewProduct/${id}`);
    }

    const increase = (id) => {
        setQuantities(prev => ({
            ...prev, [id]: prev[id] + 1
        }));
    };

    const decrease = (id) => {
        setQuantities(prev => ({
            ...prev, [id]: prev[id] > 1 ? prev[id] - 1 : 1
        }));
    };

    const addToCart = async (product, quantity) => {
        if (!authContext.isAuthenticated || !authContext.user) {
            toast.error("Please log in to add items to your cart.")
            navigate("/login");
            return;
        }

        try {
            await addToCartApi(
                authContext.user.id,
                product.id,
                quantity,
                authContext.token
            );
            toast.success(`Added ${quantities[product.id]} x ${product.title} to cart`);
            if (authContext.loadCartCount) {
                authContext.loadCartCount();
            }
        } catch (err) {
            console.error("Failed to add to cart", err);
            toast.error("Failed to add item to cart");
        }
    };

    return (
        <div className="max-w-7xl mx-auto px-4 py-10">
            <h1 className="text-3xl font-bold text-gray-800 mb-8 text-center">
                Our Products
            </h1>

            {products.length === 0 ? (
                <p className="text-center text-gray-500">No products found.</p>
            ) : (
                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-8">
                    {products.map((product) => (
                        <div
                            key={product.id}
                            className="bg-white rounded-xl shadow-sm hover:shadow-md transition p-5 flex flex-col"
                        >
                            <div className="w-full h-48 flex items-center justify-center overflow-hidden rounded-lg bg-gray-50">
                                <img
                                    src={product.image}
                                    alt={product.title}
                                    className="object-contain h-full"
                                />
                            </div>

                            <h3 className="mt-4 text-lg font-semibold text-gray-800 line-clamp-2" onClick={() => viewProduct(product.id)}>
                                {product.title}
                            </h3>

                            <p className="mt-2 text-blue-600 font-bold text-lg">
                                {product.price} kr
                            </p>

                            <div className="flex items-center gap-3 my-4">
                                <button
                                    onClick={() => decrease(product.id)}
                                    className="w-8 h-8 flex items-center justify-center border rounded-lg bg-gray-100 text-lg disabled:opacity-50"
                                    disabled={quantities[product.id] === 1}
                                >
                                    -
                                </button>
                                <span className="w-10 text-center text-md font-medium">{quantities[product.id]}</span>
                                <button
                                    onClick={() => increase(product.id)}
                                    className="w-8 h-8 flex items-center justify-center border rounded-lg bg-gray-100 text-lg"
                                >
                                    +
                                </button>
                            </div>
                            <button
                                onClick={() => addToCart(product, quantities[product.id])}
                                className="mt-auto bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition"
                            >
                                🛒 Add to Cart
                            </button>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}