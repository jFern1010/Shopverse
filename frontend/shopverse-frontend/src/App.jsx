import Navbar from './components/Navbar'
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import Home from './pages/Home'
import Products from './pages/Products'
import Cart from './pages/Cart'
import Login from './pages/Login'
import Register from './pages/Register'
import Product from './pages/Product'
import Orders from './pages/Orders'
import Checkout from './pages/Checkout'
import Confirmation from './pages/Confirmation'
import AdminDashboardLayout from './pages/AdminDashboardLayout'
import AdminOrderPage from './pages/AdminOrderPage'
import AdminProductPage from './pages/AdminProductPage'

import AuthProvider, { useAuth } from './security/AuthContext'
import { Toaster } from 'sonner'
import SearchResults from './pages/SearchResults'

function AuthenticatedRoute({ children }) {
  const authContext = useAuth();
  if (authContext.isAuthenticated)
    return children
  return <Navigate to="/login" />
}

function AdminRoute({ children }) {
  const { isAuthenticated, isAdmin } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" />;
  }

  if (!isAdmin()) {
    return <Navigate to="/" />;
  }

  return children
}

export default function App() {

  return (
    <AuthProvider>
      <Router>
        <Navbar />
        <Routes>
          <Route path='/register' element={<Register />} />
          <Route path='/login' element={<Login />} />
          <Route path='/' element={<Home />} />
          <Route path='/products' element={<Products />} />
          <Route path='/viewProduct/:productId' element={<Product />} />

          <Route path='/cart' element={
            <AuthenticatedRoute>
              <Cart />
            </AuthenticatedRoute>} />

          <Route path='/orders' element={
            <AuthenticatedRoute>
              <Orders />
            </AuthenticatedRoute>} />

          <Route path='/checkout' element={
            <AuthenticatedRoute>
              <Checkout />
            </AuthenticatedRoute>} />

          <Route path='/confirmation/:orderId' element={
            <AuthenticatedRoute>
              <Confirmation />
            </AuthenticatedRoute>} />

          <Route path='/search' element={<SearchResults />} />

          <Route path='/admin' element={
            <AdminRoute>
              <AdminDashboardLayout />
            </AdminRoute>
          }>
            <Route index element={
              <p>Welcome to Admin Dashboard. Select a tab.</p>} />
            <Route path='products' element={<AdminProductPage />} />
            <Route path='orders' element={<AdminOrderPage />} />
          </Route>
        </Routes>
        <Toaster richColors position='top-right' />
      </Router>
    </AuthProvider>

  );

}

