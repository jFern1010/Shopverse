import { Link } from "react-router-dom";

export default function Home() {
    return (
        <div className="bg-gray-50 min-h-screen flex flex-col items-center justify-center text-center px-4">
            <h1 className="text-4xl md:text-6xl font-bold text-gray-800 mb-4">
                Welcome to <span className="text-blue-600">ShopVerse</span>
            </h1>
            <p className="text-lg text-gray-600 max-w-2xl mb-8">
                Discover the best products at unbeatable prices. Your one-stop online shop for everything you need.
            </p>
            <Link
                to="/products"
                className="bg-blue-500 text-white px-6 py-3 rounded-lg hover:bg-blue-600 transition"
            >
                Start Shopping
            </Link>
        </div>
    );
}
