package com.shopverse.backend.controllertest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopverse.backend.configuration.SecurityTestConfiguration;
import com.shopverse.backend.controllers.OrderController;
import com.shopverse.backend.dto.CheckoutRequestDTO;
import com.shopverse.backend.dto.OrderDTO;
import com.shopverse.backend.services.OrderService;

@Import(SecurityTestConfiguration.class)
@WebMvcTest(OrderController.class)
class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderService orderService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@WithMockUser(roles = "USER")
	void placeOrder_shouldReturnOrderDTO() throws Exception {
		CheckoutRequestDTO checkoutRequest = new CheckoutRequestDTO("John Doe", "123 Main St", "Oslo", "12345",
				"Norway", "John Doe", "123 Main St", "Oslo", "12345", "Norway", "CARD");

		OrderDTO dto = new OrderDTO();
		dto.setTotal(150.0);

		when(orderService.placeOrder(eq(1L), any(CheckoutRequestDTO.class))).thenReturn(dto);

		mockMvc.perform(post("/shopverse/orders/place/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(checkoutRequest))).andExpect(status().isOk())
				.andExpect(jsonPath("$.total").value(150.0));
	}

	@Test
	@WithMockUser(roles = "USER")
	void getUserOrders_shouldReturnList() throws Exception {
		OrderDTO dto = new OrderDTO();
		dto.setTotal(99.0);

		when(orderService.getOrdersByUser(1L)).thenReturn(List.of(dto));

		mockMvc.perform(get("/shopverse/orders/users/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1)).andExpect(jsonPath("$[0].total").value(99.0));
	}

	@Test
	@WithMockUser(roles = "USER")
	void getOrderById_shouldReturnOrder() throws Exception {
		OrderDTO dto = new OrderDTO();
		dto.setTotal(75.0);

		when(orderService.getOrderById(200L)).thenReturn(dto);

		mockMvc.perform(get("/shopverse/orders/200")).andExpect(status().isOk())
				.andExpect(jsonPath("$.total").value(75.0));
	}

	@Test
	@WithMockUser(roles = "USER")
	void cancelOrder_shouldReturnSuccessMessage() throws Exception {
		doNothing().when(orderService).cancelOrder(100L);

		mockMvc.perform(put("/shopverse/orders/cancel/100")).andExpect(status().isOk())
				.andExpect(content().string("Order cancelled successfully"));
	}

	@Test
	@WithMockUser(roles = "USER")
	void processPayment_shouldReturnSuccessMessage() throws Exception {
		doNothing().when(orderService).processPayment(100L);

		mockMvc.perform(put("/shopverse/orders/pay/100")).andExpect(status().isOk())
				.andExpect(content().string("Payment successful"));
	}

	// --- Negative-path examples ---

	@Test
	@WithMockUser(roles = "USER")
	void getOrderById_shouldReturnNotFound_whenMissing() throws Exception {
		when(orderService.getOrderById(999L)).thenThrow(new org.springframework.web.server.ResponseStatusException(
				org.springframework.http.HttpStatus.NOT_FOUND, "Order not found"));

		mockMvc.perform(get("/shopverse/orders/999")).andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(roles = "USER")
	void cancelOrder_shouldReturnBadRequest_whenNotCancellable() throws Exception {
		org.springframework.web.server.ResponseStatusException ex = new org.springframework.web.server.ResponseStatusException(
				org.springframework.http.HttpStatus.BAD_REQUEST, "Cannot cancel shipped order");

		org.mockito.Mockito.doThrow(ex).when(orderService).cancelOrder(100L);

		mockMvc.perform(put("/shopverse/orders/cancel/100")).andExpect(status().isBadRequest());
	}
}