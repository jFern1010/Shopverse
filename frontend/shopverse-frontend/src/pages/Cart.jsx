import { useNavigate } from "react-router-dom";
import { useAuth } from "../security/AuthContext";
import { getCartApi, removeFromCartApi } from "../services/ApiService";
import { useState, useEffect } from "react";


export default function Cart() {

    const authContext = useAuth();
    const [cart, setCart] = useState({ items: [], totalPrice: 0 });
    const [confirmModal, setConfirmModal] = useState({ open: false, productId: null, productName: "" });
    const navigate = useNavigate();

    const loadCart = () => {
        getCartApi(authContext.user.id, authContext.token)
            .then(response => {
                console.log(response.data.items);
                setCart(response.data);
                authContext.loadCartCount();
            })
            .catch((err) => console.error("Failed to load cart", err));
    };


    useEffect(() => {
        if (authContext.isAuthenticated) {
            loadCart();
        }
    }, [authContext]);

    const handleRemoveClick = (productId, productName) => {
        setConfirmModal({ open: true, productId, productName });
    };

    const confirmRemove = async () => {
        try {
            await removeFromCartApi(authContext.user.id, confirmModal.productId, authContext.token);
            setConfirmModal({ open: false, productId: null, productName: "" });
            loadCart();
        }
        catch (err) {
            console.error("Failed to remove item", err);
        }
    };

    const cancelRemove = () => {
        setConfirmModal({ open: false, productId: null, productName: "" });
    };

    const handleCheckOut = () => {
        navigate("/checkout");
    };

    if (!cart.items || cart.items.length === 0) {
        return <div className="p-6 text-lg">Your cart is empty 🛒</div>
    }

    return (
        <div className="max-w-4xl mx-auto p-6">
            <h2 className="text-2xl font-bold mb-4">Your Cart</h2>
            <ul className="space-y-4">
                {cart.items.map(item => (
                    <li key={item.id} className="flex justify-between items-center border p-4 rounded-lg shadow-sm">
                        <div>
                            <span className="font-medium">{item.productName}</span>
                            <span className="ml-2 text-gray-600">( x {item.quantity})</span>
                        </div>
                        <div className="flex items-center space-x-4">
                            <span>{(item.price * item.quantity).toFixed(2)} kr</span>
                            <button onClick={() => handleRemoveClick(item.productId, item.productName)}
                                className="text-red-600 hover:text-red-800 font-semibold"
                            >
                                Remove
                            </button>
                        </div>
                    </li>
                ))}
            </ul>
            <div className="mt-6 text-right text-xl font-semibold">
                <span>Total: {cart.totalPrice.toFixed(2)} kr</span>
                <button className="ml-4 bg-blue-600 text-white px-4 py-2 rounded-lg shadow hover:bg-blue-700 transition" onClick={handleCheckOut}>Checkout</button>
            </div>


            {confirmModal.open && (
                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-40 z-50">
                    <div className="bg-white p-6 rounded-xl shadow-lg max-w-sm w-full">
                        <h3 className="text-lg font-bold mb-4">Remove Item</h3>
                        <p className="mb-6">
                            Are you sure you want to remove {" "}
                            <span className="font-semibold">{confirmModal.productName}</span> {" "} from your cart?</p>
                        <div className="flex justify-end space-x-3">
                            <button
                                onClick={cancelRemove} className="px-4 py-2 rounded-lg border border-gray-300 hover:bg-gray-100">
                                Cancel
                            </button>
                            <button onClick={confirmRemove} className="px-4 py-2 rounded-lg bg-red-600 text-white hover:bg-red-700">
                                Remove
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}