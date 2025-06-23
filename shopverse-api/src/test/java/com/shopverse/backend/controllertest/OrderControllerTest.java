package com.shopverse.backend.controllertest;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.shopverse.backend.configuration.SecurityTestConfiguration;
import com.shopverse.backend.controllers.OrderController;
import com.shopverse.backend.dto.OrderDTO;
import com.shopverse.backend.services.OrderService;

@Import(SecurityTestConfiguration.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderService orderService;

	@Test
	@WithMockUser(roles = "USER")
	void testPlaceOrder() throws Exception {
		OrderDTO dto = new OrderDTO();
		dto.setTotal(50.0);
		when(orderService.placeOrder(1L)).thenReturn(dto);

		mockMvc.perform(post("/shopverse/orders/place/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.total").value(50.0));

	}

	@Test
	@WithMockUser(roles = "USER")
	void testGetUsersOrders() throws Exception {
		OrderDTO dto = new OrderDTO();
		dto.setTotal(99.0);

		when(orderService.getOrdersByUser(1L)).thenReturn(List.of(dto));

		mockMvc.perform(get("/shopverse/orders/users/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1)).andExpect(jsonPath("$[0].total").value(99.0));
	}

	@Test
	@WithMockUser(roles = "USER")
	void testCancelOrder() throws Exception {
		doNothing().when(orderService).cancelOrder(100L);
		
		mockMvc.perform(put("/shopverse/orders/cancel/100")).andExpect(status().isOk())
				.andExpect(content().string("Order cancelled successfully"));
		
	}

	@Test
	@WithMockUser(roles = "USER")
	void testProcessPayment() throws Exception {
		doNothing().when(orderService).processPayment(100L);

		mockMvc.perform(put("/shopverse/orders/pay/100")).andExpect(status().isOk())
				.andExpect(content().string("Payment successful"));
	}
}
