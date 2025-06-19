package com.shopverse.backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopverse.backend.dto.OrderDTO;
import com.shopverse.backend.services.OrderService;

@RestController
@RequestMapping("/shopverse/orders")
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PreAuthorize("hasRole('USER')")
	@PostMapping("/place/{userId}")
	public ResponseEntity<OrderDTO> placeOrder(@PathVariable long userId) {
		OrderDTO orderDTO = orderService.placeOrder(userId);
		return ResponseEntity.ok(orderDTO);
	}

	@PreAuthorize("hasRole('USER')")
	@GetMapping("users/{userId}")
	public ResponseEntity<List<OrderDTO>> getUserOrders(@PathVariable Long userId) {
		List<OrderDTO> orderDTOs = orderService.getOrdersByUser(userId);
		return ResponseEntity.ok(orderDTOs);
	}

	@PreAuthorize("hasRole('USER')")
	@PutMapping("/cancel/{orderId}")
	public ResponseEntity<String> cancelOrder(@PathVariable long orderId) {
		orderService.cancelOrder(orderId);
		return ResponseEntity.ok("Order cancelled successfully");
	}

	@PreAuthorize("hasRole('USER')")
	@PutMapping("/pay/{orderId}")
	public ResponseEntity<?> payment(@PathVariable long orderId) {
		orderService.processPayment(orderId);
		return ResponseEntity.ok("Payment successful");
	}

}
