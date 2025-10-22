import { useState, useEffect } from "react";
import { addToCartApi, getProductApi } from "../services/ApiService";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "sonner";
import { useAuth } from "../security/AuthContext";


export default function Product() {

    const { productId } = useParams();
    const [product, setProduct] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null)
    const [quantity, setQuantity] = useState(1);
    const authContext = useAuth();
    const navigate = useNavigate()


    useEffect(() => {
        getProductApi(productId)
            .then(response => {
                setProduct(response.data)
            })
            .catch(error => {
                console.error(error);
                setError('Failed to load product');
            })
            .finally(() => {
                setLoading(false)
            })
    }, [productId])

    const increase = () => setQuantity(prev => prev + 1);
    const decrease = () => setQuantity(prev => prev - 1);

    const addToCart = async () => {
        if (!authContext.isAuthenticated || !authContext.user) {
            navigate("/login");
            return;
        }

        try {
            await addToCartApi(
                authContext.user.id,
                product.id,
                quantity,
                authContext.token
            )
            toast.success(`Added ${quantity} x ${product.title} to cart`);
        }
        catch (err) {
            console.error("Failed to add to cart", err);
            toast.error("Failed to add to cart");
        }
    };

    if (loading) {
        return <p className="text-center text-gray-500">Loading products...</p>;
    }

    if (error) {
        return <p className="text-center text-red-500">{error}</p>;
    }

    return (
        <div className="max-w-7xl mx-auto px-4 py-10">
            <h1 className="text-3xl font-bold text-gray-800 mb-6">{product.title}</h1>

            <img
                src={product.image}
                alt={product.title}
                className="w-full max-w-md object-contain mx-auto mb-6"
            />

            <p className="text-lg text-gray-700 mb-4">{product.description}</p>
            <p className="text-2xl font-bold text-blue-600">{product.price} kr</p>

            <div className="flex items-center gap-4 mb-6">
                <button
                    onClick={decrease}
                    className="w-10 h-10 flex items-center justify-center border rounded-lg bg-gray-100 text-xl disabled:opacity-50"
                    disabled={quantity === 1}
                >
                    -
                </button>
                <span className="w-12 text-center text-lg font-medium">{quantity}</span>
                <button
                    onClick={increase}
                    className="w-10 h-10 flex items-center justify-center border rounded-lg bg-gray-100 text-xl"
                >
                    +
                </button>
            </div>

            <button onClick={addToCart} className="mt-auto bg-blue-500 text-white px-5 py-2 rounded-lg hover:bg-blue-600 transition">
                🛒 Add {quantity} to Cart
            </button>
        </div>
    );
}
