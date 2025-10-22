import { useEffect, useState } from "react";
import { useAuth } from "../security/AuthContext";
import {
    addProductApi,
    allCategoriesApi,
    allProductsApi,
    deleteProductApi,
    updateProductApi
} from "../services/ApiService";

export default function AdminProductPage() {
    const authContext = useAuth();
    const [products, setProducts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [editingProduct, setEditingProduct] = useState(null);
    const [formData, setFormData] = useState({
        title: "",
        description: "",
        price: "",
        stock: "",
        imageUrl: "",
        categoryId: ""
    });

    useEffect(() => {
        loadProducts();
        loadCategories();
    }, []);

    const loadProducts = () => {
        allProductsApi()
            .then(res => setProducts(res.data))
            .catch(err => console.error("Failed to fetch products", err));
    };

    const loadCategories = () => {
        allCategoriesApi()
            .then(res => {
                setCategories(res.data);

            })
            .catch(err => console.error("Failed to fetch categories", err))
    };

    const handleDelete = async (id) => {
        if (window.confirm("Are you sure you want to delete this product?")) {
            try {
                await deleteProductApi(id, authContext.token);
                loadProducts();
            } catch (err) {
                console.error("Delete failed", err);
            }
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            if (editingProduct) {
                await updateProductApi(editingProduct.id, formData, authContext.token);
            } else {
                await addProductApi(formData, authContext.token);
            }
            setFormData({
                title: "",
                description: "",
                price: "",
                stock: "",
                imageUrl: "",
                categoryId: ""
            });
            setEditingProduct(null);
            loadProducts();
        } catch (error) {
            console.error("Save failed", error);
        }
    };

    const handleEdit = (product) => {
        setEditingProduct(product);
        const categoryObj = categories.find(x => x.name === product.category);
        setFormData({
            title: product.title,
            description: product.description,
            price: product.price,
            stock: product.stock,
            imageUrl: product.imageUrl,
            categoryId: categoryObj ? categoryObj.id : ""
        });
    };

    return (
        <div className="p-6 max-w-6xl mx-auto">
            <h1 className="text-3xl font-bold mb-6">Products</h1>

            <div className="bg-white shadow p-4 rounded-lg mb-8">
                <h2 className="text-xl font-semibold mb-4">
                    {editingProduct ? "Edit Product" : "Add New Product"}
                </h2>
                <form onSubmit={handleSubmit} className="space-y-4">
                    <input
                        type="text"
                        placeholder="Title"
                        value={formData.title}
                        onChange={e => setFormData({ ...formData, title: e.target.value })}
                        className="w-full p-2 border rounded"
                        required
                    />

                    <textarea
                        placeholder="Description"
                        value={formData.description}
                        onChange={e => setFormData({ ...formData, description: e.target.value })}
                        className="w-full p-2 border rounded"
                        required
                    />

                    <input
                        type="number"
                        placeholder="Price"
                        value={formData.price}
                        onChange={e => setFormData({ ...formData, price: e.target.value })}
                        className="w-full p-2 border rounded"
                        required
                    />

                    <input
                        type="number"
                        placeholder="Stock"
                        value={formData.stock}
                        onChange={e => setFormData({ ...formData, stock: e.target.value })}
                        className="w-full p-2 border rounded"
                        required
                    />

                    <input
                        type="text"
                        placeholder="Image URL"
                        value={formData.imageUrl}
                        onChange={e => setFormData({ ...formData, imageUrl: e.target.value })}
                        className="w-full p-2 border rounded"
                    />

                    <select
                        value={formData.categoryId}
                        onChange={e => setFormData({ ...formData, categoryId: e.target.value })}
                        className="w-full p-2 border rounded"
                        required
                    >
                        <option value="">Select Category</option>
                        {categories.map(cat => (
                            <option key={cat.id} value={cat.id}>
                                {cat.name}
                            </option>
                        ))}
                    </select>

                    <button
                        type="submit"
                        className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition"
                    >
                        {editingProduct ? "Update" : "Add"}
                    </button>
                </form>
            </div>

            <h2 className="text-2xl font-semibold mb-4">Product List</h2>
            <div className="overflow-x-auto">
                <table className="min-w-full bg-white border rounded-lg shadow">
                    <thead className="bg-gray-100">
                        <tr>
                            <th className="text-left p-3">Title</th>
                            <th className="text-left p-3">Description</th>
                            <th className="text-left p-3">Price</th>
                            <th className="text-left p-3">Stock</th>
                            <th className="text-left p-3">Category</th>
                            <th className="text-left p-3">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {products.map(product => (
                            <tr key={product.id} className="border-t">
                                <td className="p-3 font-semibold">{product.title}</td>
                                <td className="p-3">{product.description}</td>
                                <td className="p-3">{product.price} kr</td>
                                <td className="p-3">
                                    <div className="flex items-center gap-2">
                                        <span className={`px-2 py-1 rounded text-sm font-semibold ${product.stock > 10 ? 'bg-green-200 text-green-800' :
                                            product.stock > 0 ? 'bg-yellow-200 text-yellow-800' :
                                                'bg-red-200 text-red-800'
                                            }`}>
                                            {product.stock} in stock
                                        </span>
                                        {product.stock <= 5 && (
                                            <span className="text-red-600 font-bold text-sm">⚠ Low Stock</span>
                                        )}
                                    </div>
                                </td>
                                <th className="p-3">{product.category}</th>
                                <td className="p-3 flex gap-2">
                                    <button
                                        onClick={() => handleEdit(product)}
                                        className="bg-yellow-500 text-white px-3 py-1 rounded hover:bg-yellow-600"
                                    >
                                        Edit
                                    </button>
                                    <button
                                        onClick={() => handleDelete(product.id)}
                                        className="bg-red-600 text-white px-3 py-1 rounded hover:bg-red-700"
                                    >
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>

        </div >
    );
}
