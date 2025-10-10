package com.shopverse.backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopverse.backend.dto.OrderDTO;
import com.shopverse.backend.services.OrderService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/shopverse/admin/orders")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {

	private final OrderService orderService;

	public AdminOrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping
	@Operation(summary = "Get all orders", description = "Retrieve all orders placed by all users")
	public ResponseEntity<List<OrderDTO>> getAllOrders() {
		List<OrderDTO> orders = orderService.getAllOrders();
		return ResponseEntity.ok(orders);
	}

	@PutMapping("/cancel/{orderId}")
	@Operation(summary = "Cancel order (admin)", description = "Admin can cancel any order by ID")
	public ResponseEntity<String> cancelOrder(@PathVariable long orderId) {
		orderService.cancelOrder(orderId);
		return ResponseEntity.ok("Order cancelled by Admin");
	}

	@PutMapping("/ship/{orderId}")
	@Operation(summary = "Mark order as shipped", description = "Admin marks order as shipped")
	public ResponseEntity<String> markAsShipped(@PathVariable long orderId) {
		orderService.markAsShipped(orderId);
		return ResponseEntity.ok("Order marked as shipped");
	}

	@PutMapping("/deliver/{orderId}")
	@Operation(summary = "Mark order as delivered", description = "Admin marks order as delivered")
	public ResponseEntity<String> markAsDelivered(@PathVariable long orderId) {
		orderService.markAsDelivered(orderId);
		return ResponseEntity.ok("Order marked as delivered");
	}
}
