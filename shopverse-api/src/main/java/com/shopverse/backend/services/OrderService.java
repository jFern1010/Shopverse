package com.shopverse.backend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.shopverse.backend.dto.OrderDTO;
import com.shopverse.backend.dto.OrderItemDTO;
import com.shopverse.backend.models.Cart;
import com.shopverse.backend.models.CartItem;
import com.shopverse.backend.models.Order;
import com.shopverse.backend.models.OrderItem;
import com.shopverse.backend.models.PaymentStatus;
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
	private CartItemRepository cartItemRepo;

	public OrderService(OrderRepository orderRepo, OrderItemRepository orderItemRepo, UserRepository userRepo,
			CartRepository cartRepo, CartItemRepository cartItemRepo, ProductRepository productRepo) {
		this.orderRepo = orderRepo;
		this.orderItemRepo = orderItemRepo;
		this.userRepo = userRepo;
		this.cartRepo = cartRepo;
		this.cartItemRepo = cartItemRepo;
		this.productRepo = productRepo;
	}

	@Transactional
	public OrderDTO placeOrder(Long userId) {
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

		List<OrderItemDTO> orderItemDTOs = orderItems.stream()
				.map(orderItem -> new OrderItemDTO(orderItem.getId(), orderItem.getProduct().getTitle(),
						orderItem.getQuantity(), orderItem.getPrice(), orderItem.getProduct().getImageUrl()))
				.collect(Collectors.toList());

		OrderDTO orderDTO = new OrderDTO(order.getId(), order.getOrderDate(), order.getStatus(), order.getTotal(),
				orderItemDTOs);
		return orderDTO;

	}

	public List<OrderDTO> getOrdersByUser(Long userId) {
		
		List <Order> orders = orderRepo.findByUserId(userId);
		
		return orders.stream().map(order -> {
			List <OrderItemDTO> itemDTOs = order.getOrderItems().stream()
					.map(item -> new OrderItemDTO(
							item.getId(),
							item.getProduct().getTitle(),
							item.getQuantity(),
							item.getPrice(),							
							item.getProduct().getImageUrl()
					))
					.collect(Collectors.toList());
		
		return new OrderDTO(
				order.getId(),
			order.getOrderDate(),
			order.getStatus(),
			order.getTotal(),
			itemDTOs
			);
		
		}).collect(Collectors.toList());		
	}

	public void cancelOrder(Long orderId) {
		Order order = orderRepo.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));

		if (!order.getStatus().equals(Status.PENDING)) {
			throw new IllegalArgumentException("Only pending orders can be cancelled");
		}

		order.setStatus(Status.CANCELLED);
		orderRepo.save(order);
	}

	public void processPayment(long orderId) {
		Order order = orderRepo.findById(orderId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

		boolean isAllinStock = order.getOrderItems().stream()
				.allMatch(item -> item.getProduct().getStock() >= item.getQuantity());

		if (!isAllinStock) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock for one or more items.");
		}

		if (order.getPaymentStatus() == PaymentStatus.PAID) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order is paid");
		}

		order.setPaymentStatus(PaymentStatus.PAID);
		order.setPaymentTimeStamp(LocalDateTime.now());
		orderRepo.save(order);
	}
}
