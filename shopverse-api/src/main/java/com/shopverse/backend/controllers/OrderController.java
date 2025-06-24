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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/shopverse/orders")
@Tag(name = "Orders", description = "Operations related to order processing")
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PreAuthorize("hasRole('USER')")
	@PostMapping("/place/{userId}")
	@Operation(summary = "Place an order", description = "Converts the user's cart into an order")
	public ResponseEntity<OrderDTO> placeOrder(@PathVariable long userId) {
		OrderDTO orderDTO = orderService.placeOrder(userId);
		return ResponseEntity.ok(orderDTO);
	}

	@PreAuthorize("hasRole('USER')")
	@GetMapping("users/{userId}")
	@Operation(summary = "Get user orders", description = "Returns a list of orders placed by the user")
	public ResponseEntity<List<OrderDTO>> getUserOrders(@PathVariable Long userId) {
		List<OrderDTO> orderDTOs = orderService.getOrdersByUser(userId);
		return ResponseEntity.ok(orderDTOs);
	}

	@PreAuthorize("hasRole('USER')")
	@PutMapping("/cancel/{orderId}")
	@Operation(summary = "Cancel order", description = "Cancels an order by ID if eligible")
	public ResponseEntity<String> cancelOrder(@PathVariable long orderId) {
		orderService.cancelOrder(orderId);
		return ResponseEntity.ok("Order cancelled successfully");
	}

	@PreAuthorize("hasRole('USER')")
	@PutMapping("/pay/{orderId}")
	@Operation(summary = "Pay for order", description = "Process payment for a given order ID")
	public ResponseEntity<?> payment(@PathVariable long orderId) {
		orderService.processPayment(orderId);
		return ResponseEntity.ok("Payment successful");
	}

}
