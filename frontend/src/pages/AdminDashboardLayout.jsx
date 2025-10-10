import { Link, Outlet } from "react-router-dom";

export default function AdminDashboardLayout() {
    return (
        <div className="p-6">
            <h1 className="text-3xl font-bold mb-6">Admin Dashboard</h1>
            <nav className="flex space-x-4 mb-6">
                <Link to="/admin/products" className="text-blue-600 hover:underline">
                    Products
                </Link>
                <Link to="/admin/orders" className="text-blue-600 hover:underline">
                    Orders
                </Link>
            </nav>
            <div className="border p-4 rounded-lg shadow-md bg-white">
                <Outlet />
            </div>
        </div>
    );
}