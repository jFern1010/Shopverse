import { apiClient } from "./ApiClient";

export const executeJwtAuthenticationServiceApi =
(email, password) => apiClient.post("/auth/login", {email, password});