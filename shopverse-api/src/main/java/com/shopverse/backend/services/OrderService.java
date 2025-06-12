package com.shopverse.backend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.shopverse.backend.models.Cart;
import com.shopverse.backend.models.CartItem;
import com.shopverse.backend.models.Order;
import com.shopverse.backend.models.OrderItem;
import com.shopverse.backend.models.Product;
import com.shopverse.backend.models.Status;
import com.shopverse.backend.repositories.CartItemRepository;
import com.shopverse.backend.repositories.CartRepository;
import com.shopverse.backend.repositories.OrderItemRepository;
import com.shopverse.backend.repositories.OrderRepository;
import com.shopverse.backend.repositories.ProductRepository;
import com.shopverse.backend.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

	private OrderRepository orderRepo;
	private OrderItemRepository orderItemRepo;
	private UserRepository userRepo;
	private CartRepository cartRepo;
	private ProductRepository productRepo;

	public OrderService(OrderRepository orderRepo, OrderItemRepository orderItemRepo, UserRepository userRepo,
			CartRepository cartRepo, CartItemRepository cartItemRepo, ProductRepository productRepo) {
		this.orderRepo = orderRepo;
		this.orderItemRepo = orderItemRepo;
		this.userRepo = userRepo;
		this.cartRepo = cartRepo;
		this.productRepo = productRepo;
	}

	@Transactional
	public Order placeOrder(Long userId) {
		Cart cart = cartRepo.findByUserId(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Cart not found"));
		
		List <CartItem> cartItems = cart.getItems();
		if (cartItems.isEmpty()) {
			throw new IllegalStateException("Cart is empty");
		}
		
		Order order = new Order();
		order.setUser(cart.getUser());
		order.setStatus(Status.PENDING);
		order.setOrderDate(LocalDateTime.now());
		
		List <OrderItem> orderItems = cartItems.stream()
				.map(cartItem -> {
						Product product = cartItem.getProduct();
						int quantity =   cartItem.getQuantity();

					OrderItem orderItem = new OrderItem();
					orderItem.setOrder(order);
					orderItem.setProduct(product);
					orderItem.setPrice(product.getPrice());
					orderItem.setQuantity(quantity);

					return orderItem;
				}).collect(Collectors.toList());

		order.setOrderItems(orderItems);

		double total = orderItems.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
		order.setTotal(total);

		orderRepo.save(order);
		orderItemRepo.saveAll(orderItems);

		cart.getItems().clear();
		cartRepo.save(cart);

		return order;
	}

	public List<Order> getOrdersByUser(Long userId) {
		return orderRepo.findByUserId(userId);
	}

	public void cancelOrder(Long orderId) {
		Order order = orderRepo.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));

		if (!order.getStatus().equals(Status.PENDING)) {
			throw new IllegalArgumentException("Only pending orders can be cancelled");
		}

		order.setStatus(Status.CANCELLED);
		orderRepo.save(order);
	}
}
