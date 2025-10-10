import { Link, useParams } from "react-router-dom"
import { useAuth } from "../security/AuthContext";
import { useState, useEffect } from "react";
import { getOrderByIdApi } from "../services/ApiService";

export default function Confirmation() {

    const { orderId } = useParams();
    const { user, token } = useAuth();
    const [order, setOrder] = useState(null);

    useEffect(() => {
        getOrderByIdApi(orderId, token)
            .then((res) => setOrder(res.data))
            .catch((err) => console.error(err));
    }, [orderId, token]);

    if (!order) {
        return (
            <div className="max-w-2xl mx-auto p-6 text-center">
                <h2 className="text-xl font-semibold">Loading order details...</h2>
            </div>
        );
    }

    return (
        <div className="max-w-3xl mx-auto p-6">
            <div className="text-center mb-6">
                <h2 className="text-3xl font-bold mb-2">✅ Order Confirmed!</h2>
                <p className="text-lg">
                    Your order <strong>#{order.id}</strong> has been placed and paid.
                </p>
                <p className="mt-1 text-gray-600">
                    A confirmation email has been sent to you.
                </p>
            </div>

            <div className="grid md:grid-cols-2 gap-6 mb-6">
                <div className="border rounded-lg p-4 shadow-sm">
                    <h3 className="font-bold mb-2">Shipping Address</h3>
                    <p>{order.shippingName}</p>
                    <p>{order.shippingStreet}</p>
                    <p>
                        {order.shippingZip} {order.shippingCity}
                    </p>
                    <p>{order.shippingCountry}</p>
                </div>
                <div className="border rounded-lg p-4 shadow-sm">
                    <h3 className="font-bold mb-2">Billing Address</h3>
                    <p>{order.billingName}</p>
                    <p>{order.billingStreet}</p>
                    <p>
                        {order.billingZip} {order.billingCity}
                    </p>
                    <p>{order.billingCountry}</p>
                </div>
            </div>

            {/* Payment */}
            <div className="mb-6 border rounded-lg p-4 shadow-sm">
                <h3 className="font-bold mb-2">Payment Method</h3>
                <p>{order.paymentMethod}</p>
                <p className="text-green-600 font-semibold mt-1">
                    Status: {order.paymentStatus}
                </p>
            </div>

            {/* Items */}
            <div className="mb-6 border rounded-lg p-4 shadow-sm">
                <h3 className="font-bold mb-2">Items</h3>
                <ul className="divide-y">
                    {order.items.map((item) => (
                        <li key={item.id} className="flex justify-between py-2">
                            <span>
                                {item.productName} (x{item.quantity})
                            </span>
                            <span>{(item.price * item.quantity).toFixed(2)} kr</span>
                        </li>
                    ))}
                </ul>
                <div className="mt-3 text-right font-bold">
                    Total: {order.total.toFixed(2)} kr
                </div>
            </div>

            <div className="text-center">
                <Link
                    to="/orders"
                    className="mt-6 inline-block bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700"
                >
                    View My Orders
                </Link>
            </div>
        </div>
    );
}