import { apiClient } from "./ApiClient";

export const allProductsApi = 
() => apiClient.get(`/products`)

export const getProductApi = 
(productId) => apiClient.get(`/products/${productId}`)

export const addToCartApi = 
(userId, productId, quantity, token) => apiClient.post(`/cart/add/${userId}/${productId}/${quantity}`, {},
    {
        headers: {Authorization: `Bearer ${token}`}
    }
)

export const getCartApi = 
(userId, token) => apiClient.get(`/cart/${userId}`, {
    headers: {
        Authorization: `Bearer ${token}`}
    }
);

export const removeFromCartApi = 
(userId,productId, token) => apiClient.delete(`/cart/remove/${userId}/${productId}`, {
    headers: {
        Authorization: `Bearer ${token}`}
    }
);

export const getOrderByIdApi = 
(orderId, token) => apiClient.get(`/orders/${orderId}`, {
    headers: {
        Authorization: `Bearer ${token}`}
});

export const getUserOrdersApi =
(userId,token) => apiClient.get(`/orders/users/${userId}`, {
    headers: {Authorization: `Bearer ${token}`}
});

export const placeOrderApi = 
(userId, checkoutRequest, token) => apiClient.post(`/orders/place/${userId}`,checkoutRequest, {
    headers: {Authorization: `Bearer ${token}`}
});

export const cancelOrderApi =
(orderId, token) => apiClient.put(`/orders/cancel/${orderId}`, {}, {
    headers: {Authorization: `Bearer ${token}`}
});

export const payOrderApi =
(orderId, token) => apiClient.put(`/orders/pay/${orderId}`, {}, {
    headers: {Authorization: `Bearer ${token}`}
});


/*Admin APIs*/

export const addProductApi = 
(product, token) => apiClient.post("/products", product, {
    headers: {Authorization: `Bearer ${token}`}
});

export const updateProductApi = 
(productId, product, token) => apiClient.put(`/products/${productId}`, product, {
    headers: {Authorization: `Bearer ${token}`}
});

export const deleteProductApi =
(productId, token) => apiClient.delete(`/products/${productId}`, {
    headers: {Authorization: `Bearer ${token}`}
});


/*Admin Order APIs*/

export const getAllOrdersApi =
(token) => apiClient.get(`/admin/orders`, {
    headers: {Authorization: `Bearer ${token}`}
});

export const adminCancelOrderApi = 
(orderId, token) => apiClient.put(`/admin/orders/cancel/${orderId}`, {}, {
    headers: {Authorization: `Bearer ${token}`}
});

export const shipOrderApi = 
(orderId, token) => apiClient.put(`/admin/orders/ship/${orderId}`, {}, {
    headers: {Authorization: `Bearer ${token}`}
});

export const deliverOrderApi = 
(orderId, token) => apiClient.put(`/admin/orders/deliver/${orderId}`, {}, {
    headers: {Authorization: `Bearer ${token}`}
});

export const searchProductsApi = 
(query) => apiClient.get(`/products/search?q=${encodeURIComponent(query)}`);

export const allCategoriesApi = 
() => apiClient.get(`/categories`)