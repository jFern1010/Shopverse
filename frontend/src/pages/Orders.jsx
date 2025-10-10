import { useEffect, useState } from "react";
import { useAuth } from "../security/AuthContext";
import { cancelOrderApi, getUserOrdersApi, payOrderApi } from "../services/ApiService"
import { toast } from "sonner";

export default function Orders({ userId }) {

    const { token } = useAuth();
    const [orders, setOrders] = useState([]);

    const fetchOrders = async () => {
        try {
            const response = await getUserOrdersApi(userId, token);
            setOrders(response.data);
        } catch (error) {
            console.error("Failed to load orders", error);
        }
    };

    useEffect(() => {
        if (userId) fetchOrders();
    }, [userId]);

    const handleCancel = async (orderId) => {
        try {
            await cancelOrderApi(orderId, token);
            toast.success("Order cancelled");
            fetchOrders();
        } catch (error) {
            toast.error(error.response?.data?.message || "Cancel failed");
        }
    };

    const handlePay = async (orderId) => {
        try {
            await payOrderApi(orderId, token);
            toast.success("Payment successful");
            fetchOrders();
        } catch (error) {
            toast.error(error.response?.data?.message || "Payment failed")
        }
    };

    return (
        <div className="grid gap-4 mt-6">
            {orders.map(order => (
                <div
                    key={order.id}
                    className="border rounded-lg shadow p-4 bg-white"
                >
                    <h2 className="text-lg font-semibold">Order #{order.id}</h2>
                    <p>Status: {order.status}</p>
                    <p>Total: {order.total} kr</p>

                    <ul className="list-disc pl-5 mt-2">
                        {order.items?.map(item => (
                            <li key={item.id}>
                                {item.productName} x {item.quantity}
                            </li>
                        ))}
                    </ul>

                    <div className="flex gap-2 mt-4">
                        <button
                            className="bg-red-500 text-white px-4 py-2 rounded disabled:opacity-50"
                            onClick={() => handleCancel(order.id)}
                            disabled={order.status !== "PENDING"}
                        >
                            Cancel
                        </button>

                        <button
                            className="bg-blue-500 text-white px-4 py-2 rounded disabled:opacity-50"
                            onClick={() => handlePay(order.id)}
                            disabled={order.status !== "PENDING"}
                        >
                            Pay
                        </button>
                    </div>
                </div>
            ))}
        </div>
    );
}