import { useEffect, useState } from "react";
import { useAuth } from "../security/AuthContext";
import { adminCancelOrderApi, deliverOrderApi, getAllOrdersApi, shipOrderApi } from "../services/ApiService";
import { toast } from "sonner";

export default function AdminOrderPage() {

    const { token } = useAuth();
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchOrders();
    }, []);

    const fetchOrders = async () => {
        try {
            setLoading(true);
            const response = await getAllOrdersApi(token);
            setOrders(response.data);
        } catch (error) {
            console.error("Failed to fetch orders", error);
            toast.error("Failed to load orders");
        } finally {
            setLoading(false)
        }
    };

    const handleCancel = async (orderId) => {
        try {
            await adminCancelOrderApi(orderId, token);
            toast.success("Order cancelled");
            fetchOrders();
        } catch (error) {
            console.error(error);
            toast.error("Failed to cancel order");
        }
    };

    const handleShip = async (orderId) => {
        try {
            await shipOrderApi(orderId, token);
            toast.success("Order shipped");
            fetchOrders();
        } catch (error) {
            console.error(error);
            toast.error("Faile to ship order");
        }
    };

    const handleDeliver = async (orderId) => {
        try {
            await deliverOrderApi(orderId, token);
            toast.success("Order delivered");
            fetchOrders();
        } catch (error) {
            console.error(error);
            toast.error("Failed to deliver order");
        }
    }

    if (loading)
        return <p className="text-center mt-10">Loading orders...</p>


    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-4">Client Orders</h1>
            {orders.length === 0 ? (
                <p>No orders found.</p>
            ) : (
                <table className="w-full border border-gray-300 shadow-md rounded-lg">
                    <thead className="bg-gray-100">
                        <tr>
                            <th className="border p-2">ID</th>
                            <th className="border p-2">Customer ID</th>
                            <th className="border p-2">Status</th>
                            <th className="border p-2">Total</th>
                            <th className="border p-2">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {orders.map((order) => (
                            <tr key={order.id}>
                                <td className="border p-2">{order.id}</td>
                                <td className="border p-2">{order.user?.id ?? "N/A"}</td>
                                <td className="border p-2">{order.status}</td>
                                <td className="border p-2">{order.total?.toFixed(2)}</td>
                                <td className="border p-2 space-x-2">
                                    {order.status === "PENDING" && (
                                        <>
                                            <button className="bg-red-500 text-white px-3 py-1 rounded"
                                                onClick={() => handleCancel(order.id)}
                                            >
                                                Cancel
                                            </button>
                                            <button className="bg-blue-500 text-white px-3 py-1 rounded"
                                                onClick={() => handleShip(order.id)}
                                            >
                                                Ship
                                            </button>
                                        </>
                                    )}
                                    {order.status === "SHIPPED" && (
                                        <button
                                            className="bg-green-500 text-white px-3 py-1 rounded"
                                            onClick={() => handleDeliver(order.id)}
                                        >
                                            Deliver
                                        </button>
                                    )}
                                    {order.status === "DELIVERED" && (
                                        <span className="text-gray-500">Completed</span>
                                    )}
                                    {order.status === "CANCELLED" && (
                                        <span className="text-gray-500">Cancelled</span>
                                    )}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}