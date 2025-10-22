import { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import { searchProductsApi } from "../services/ApiService";

export default function SearchResults() {

    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const location = useLocation();
    const query = new URLSearchParams(location.search).get("q");

    useEffect(() => {
        if (!query) return;

        setLoading(true);
        searchProductsApi(query)
            .then((res) => {
                setResults(res.data)
                setLoading(false);
            })
            .catch((err) => {
                setError(err.message || "Failed to fetch results");
                setLoading(false);
            });
    }, [query]);

    if (!query) return <p className="p-4">No search quesry provided.</p>
    if (loading) return <p className="p-4">Loading...</p>
    if (error) return <p className="p-4 text-red-500">Error: {error} </p>;

    return (
        <div className="max-w-7xl mx-auto p-4">
            <h1 className="text-2xl font-bold mb-4">
                Search Results for "{query}"
            </h1>
            {results.length === 0 ? (
                <p>No products found.</p>
            ) : (
                <ul className="grid grid-cols-1 md:grid-cols-3 gap-6">
                    {results.map((product) => (
                        <li key={product.id} className="border p-4 rounded-lg shadow">
                            <img
                                src={product.image}
                                alt={product.title}
                                className="w-full h-48 object-contain mb-4"
                            />
                            <h2 className="font-semibold"> {product.title}</h2>
                            <p className="text-gray-600"> {product.description}</p>
                            <p className="text-blue-600 font-bold">{product.price} kr</p>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}