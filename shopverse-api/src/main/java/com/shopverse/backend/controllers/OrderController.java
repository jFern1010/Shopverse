package com.shopverse.backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopverse.backend.models.Order;
import com.shopverse.backend.services.OrderService;

@RestController
@RequestMapping("/shopverse/orders")
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("/place/{userId}")
	public ResponseEntity<Order> placeOrder(@PathVariable long userId) {
		Order order = orderService.placeOrder(userId);
		return ResponseEntity.ok(order);
	}

	@GetMapping("users/{userId}")
	public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
		List<Order> orders = orderService.getOrdersByUser(userId);
		return ResponseEntity.ok(orders);
	}

	@PutMapping("/cancel/{orderId}")
	public ResponseEntity<String> cancelOrder(@PathVariable long orderId) {
		orderService.cancelOrder(orderId);
		return ResponseEntity.ok("Order cancelled successfully");
	}

}
