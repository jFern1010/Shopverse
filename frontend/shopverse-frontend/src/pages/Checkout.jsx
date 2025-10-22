import { useState, useEffect } from "react";
import { useAuth } from "../security/AuthContext";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { getCartApi, payOrderApi, placeOrderApi } from "../services/ApiService";


export default function Checkout() {

    const { user, token, loadCartCount } = useAuth();
    const [cart, setCart] = useState({ items: [], totalPrice: 0 });
    const navigate = useNavigate();

    const [shipping, setShipping] = useState({
        name: "",
        street: "",
        city: "",
        zip: "",
        country: "",
    });

    const [billing, setBilling] = useState({
        name: "",
        street: "",
        city: "",
        zip: "",
        country: "",
    });

    const [useShippingAsBilling, setUseShippingAsBilling] = useState(true);
    const [payment, setPayment] = useState("CREDIT_CARD");

    useEffect(() => {
        getCartApi(user.id, token)
            .then(res => setCart(res.data))
            .catch(err => console.error(err));
    }, [user.id, token]);

    const handlePlaceOrder = async () => {
        try {
            const billingData = useShippingAsBilling ? shipping : billing;

            const checkoutRequest = {
                shippingName: shipping.name,
                shippingStreet: shipping.street,
                shippingCity: shipping.city,
                shippingZip: shipping.zip,
                shippingCountry: shipping.country,

                billingName: billingData.name,
                billingStreet: billingData.street,
                billingCity: billingData.city,
                billingZip: billingData.zip,
                billingCountry: billingData.country,

                paymentMethod: payment
            }
            const orderRes = await placeOrderApi(user.id, checkoutRequest, token);
            const order = orderRes.data;

            await payOrderApi(order.id, token);

            toast.success(`Order #${order.id} placed & paid successfully! 🎉`);
            loadCartCount();
            navigate(`/confirmation/${order.id}`);
        } catch (error) {
            toast.error(error.response?.data?.message || "Checkout failed ❌");
        }
    };

    const handleChange = (setter) => (e) => {
        const { name, value } = e.target;
        setter((prev) => ({ ...prev, [name]: value }));
    };

    return (
        <div className="max-w-3xl mx-auto p-6">
            <h2 className="text-2xl font-bold mb-6">Checkout</h2>

            <div className="mb-6">
                <h3 className="text-lg font-semibold mb-2">Shipping Address</h3>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <input className="w-full border rounded p-2" name="name" placeholder="Full Name" value={shipping.name} onChange={handleChange(setShipping)}
                    />
                    <input
                        className="border rounded p-2"
                        name="street"
                        placeholder="Street Address"
                        value={shipping.street}
                        onChange={handleChange(setShipping)}
                    />
                    <input
                        className="border rounded p-2"
                        name="city"
                        placeholder="City"
                        value={shipping.city}
                        onChange={handleChange(setShipping)}
                    />
                    <input
                        className="border rounded p-2"
                        name="zip"
                        placeholder="ZIP Code"
                        value={shipping.zip}
                        onChange={handleChange(setShipping)}
                    />
                    <input
                        className="border rounded p-2"
                        name="country"
                        placeholder="Country"
                        value={shipping.country}
                        onChange={handleChange(setShipping)}
                    />
                </div>
            </div>

            <div className="mb-6">
                <h3 className="text-lg font-semibold mb-2 flex items-center justify-between">
                    Billing Address
                    <label className="text-sm font-normal">
                        <input
                            type="checkbox"
                            checked={useShippingAsBilling}
                            onChange={(e) => setUseShippingAsBilling(e.target.checked)}
                            className="mr-2"
                        />
                        Same as shipping
                    </label>
                </h3>

                {!useShippingAsBilling && (
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <input
                            className="border rounded p-2"
                            name="name"
                            placeholder="Full Name"
                            value={billing.name}
                            onChange={handleChange(setBilling)}
                        />
                        <input
                            className="border rounded p-2"
                            name="street"
                            placeholder="Street Address"
                            value={billing.street}
                            onChange={handleChange(setBilling)}
                        />
                        <input
                            className="border rounded p-2"
                            name="city"
                            placeholder="City"
                            value={billing.city}
                            onChange={handleChange(setBilling)}
                        />
                        <input
                            className="border rounded p-2"
                            name="zip"
                            placeholder="ZIP Code"
                            value={billing.zip}
                            onChange={handleChange(setBilling)}
                        />
                        <input
                            className="border rounded p-2"
                            name="country"
                            placeholder="Country"
                            value={billing.country}
                            onChange={handleChange(setBilling)}
                        />
                    </div>
                )}
            </div>

            <div className="mb-6">
                <h3 className="text-lg font-semibold mb-2">Payment Method</h3>
                <select
                    className="w-full border rounded p-2"
                    value={payment}
                    onChange={e => setPayment(e.target.value)}
                >
                    <option value="CREDIT_CARD">Credit Card</option>
                    <option value="PAYPAL">PayPal</option>
                    <option value="COO">Cash on Delivery</option>
                </select>
            </div>

            <div className="mt-6 border p-4 rounded-lg shadow-sm">
                <h3 className="font-bold mb-2">Order Summary</h3>
                <ul className="space-y-2">
                    {cart.items.map(item => (
                        <li key={item.id} className="flex justify-between">
                            <span>{item.productName} (x {item.quantity})</span>
                            <span>{(item.price * item.quantity).toFixed(2)} kr</span>
                        </li>
                    ))}
                </ul>
                <div className="mt-4 font-semibold">
                    Total: {cart.totalPrice.toFixed(2)} kr
                </div>
            </div>

            <button onClick={handlePlaceOrder} className="mt-6 bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700"
            >
                Confirm & Pay
            </button>
        </div>
    );
}