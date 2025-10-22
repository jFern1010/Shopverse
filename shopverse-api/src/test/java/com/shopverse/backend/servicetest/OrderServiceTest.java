package com.shopverse.backend.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.shopverse.backend.dto.CheckoutRequestDTO;
import com.shopverse.backend.dto.OrderDTO;
import com.shopverse.backend.models.Cart;
import com.shopverse.backend.models.CartItem;
import com.shopverse.backend.models.Order;
import com.shopverse.backend.models.OrderItem;
import com.shopverse.backend.models.PaymentStatus;
import com.shopverse.backend.models.Product;
import com.shopverse.backend.models.Status;
import com.shopverse.backend.models.User;
import com.shopverse.backend.repositories.CartItemRepository;
import com.shopverse.backend.repositories.CartRepository;
import com.shopverse.backend.repositories.OrderItemRepository;
import com.shopverse.backend.repositories.OrderRepository;
import com.shopverse.backend.repositories.ProductRepository;
import com.shopverse.backend.repositories.UserRepository;
import com.shopverse.backend.services.OrderService;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	private OrderRepository orderRepo;
	@Mock
	private OrderItemRepository orderItemRepo;
	@Mock
	private UserRepository userRepo;
	@Mock
	private CartRepository cartRepo;
	@Mock
	private ProductRepository productRepo;
	@Mock
	private CartItemRepository cartItemRepo;

	@InjectMocks
	private OrderService orderService;

	private User user;
	private Product product;
	private Cart cart;
	private CartItem cartItem;
	private CheckoutRequestDTO checkoutRequest;

	@BeforeEach
	void setUp() {
		user = new User();
		user.setId(1L);

		product = new Product();
		product.setId(100L);
		product.setTitle("Test Product");
		product.setPrice(20.0);
		product.setStock(5);

		cartItem = new CartItem();
		cartItem.setProduct(product);
		cartItem.setQuantity(2);

		cart = new Cart();
		cart.setUser(user);
		cart.setItems(new ArrayList<>(List.of(cartItem)));

		checkoutRequest = new CheckoutRequestDTO("John Doe", "123 Main St", "Oslo", "12345", "Norway", "John Doe",
				"123 Main St", "Oslo", "12345", "Norway", "CARD");
	}

	// --- placeOrder ---
	@Test
	void placeOrder_success() {
		when(cartRepo.findByUserId(1L)).thenReturn(Optional.of(cart));

		OrderDTO result = orderService.placeOrder(1L, checkoutRequest);

		assertNotNull(result);
		assertEquals(40.0, result.getTotal());
		verify(orderRepo).save(any(Order.class));
		verify(orderItemRepo).saveAll(any());
		verify(cartRepo).save(any(Cart.class));
	}

	@Test
	void placeOrder_shouldThrowIfCartMissing() {
		when(cartRepo.findByUserId(1L)).thenReturn(Optional.empty());
		assertThrows(ResponseStatusException.class, () -> orderService.placeOrder(1L, checkoutRequest));
	}

	@Test
	void placeOrder_shouldThrowIfCartEmpty() {
		cart.setItems(new ArrayList<>());
		when(cartRepo.findByUserId(1L)).thenReturn(Optional.of(cart));
		assertThrows(IllegalStateException.class, () -> orderService.placeOrder(1L, checkoutRequest));
	}

	// --- cancelOrder ---
	@Test
	void cancelOrder_shouldUpdateStatus() {
		Order order = new Order();
		order.setId(100L);
		order.setStatus(Status.PENDING);
		when(orderRepo.findById(100L)).thenReturn(Optional.of(order));

		orderService.cancelOrder(100L);

		assertEquals(Status.CANCELLED, order.getStatus());
		verify(orderRepo).save(order);
	}

	@Test
	void cancelOrder_shouldThrowIfNotPending() {
		Order order = new Order();
		order.setStatus(Status.SHIPPED);
		when(orderRepo.findById(100L)).thenReturn(Optional.of(order));
		assertThrows(IllegalArgumentException.class, () -> orderService.cancelOrder(100L));
	}

	@Test
	void cancelOrder_shouldThrowIfOrderMissing() {
		when(orderRepo.findById(100L)).thenReturn(Optional.empty());
		assertThrows(IllegalArgumentException.class, () -> orderService.cancelOrder(100L));
	}

	// --- processPayment ---
	@Test
	void processPayment_successful() {
		OrderItem item = new OrderItem();
		item.setProduct(product);
		item.setQuantity(2);

		Order order = new Order();
		order.setId(100L);
		order.setStatus(Status.PENDING);
		order.setPaymentStatus(PaymentStatus.PENDING);
		order.setOrderItems(List.of(item));

		when(orderRepo.findById(100L)).thenReturn(Optional.of(order));

		orderService.processPayment(100L);

		assertEquals(PaymentStatus.PAID, order.getPaymentStatus());
		assertNotNull(order.getPaymentTimeStamp());
		verify(orderRepo).save(order);
	}

	@Test
	void processPayment_shouldThrowIfOrderMissing() {
		when(orderRepo.findById(100L)).thenReturn(Optional.empty());
		assertThrows(ResponseStatusException.class, () -> orderService.processPayment(100L));
	}

	@Test
	void processPayment_shouldThrowIfInsufficientStock() {
		product.setStock(1);
		OrderItem item = new OrderItem();
		item.setProduct(product);
		item.setQuantity(2);

		Order order = new Order();
		order.setPaymentStatus(PaymentStatus.PENDING);
		order.setOrderItems(List.of(item));

		when(orderRepo.findById(100L)).thenReturn(Optional.of(order));

		assertThrows(ResponseStatusException.class, () -> orderService.processPayment(100L));
	}

	@Test
	void processPayment_shouldThrowIfAlreadyPaid() {
		Order order = new Order();
		order.setPaymentStatus(PaymentStatus.PAID);
		order.setOrderItems(List.of());

		when(orderRepo.findById(100L)).thenReturn(Optional.of(order));

		assertThrows(ResponseStatusException.class, () -> orderService.processPayment(100L));
	}

	// --- getOrderById ---
	@Test
	void getOrderById_shouldReturnDTO() {
		Order order = new Order();
		order.setId(200L);
		order.setTotal(50.0);
		when(orderRepo.findById(200L)).thenReturn(Optional.of(order));

		OrderDTO dto = orderService.getOrderById(200L);
		assertEquals(50.0, dto.getTotal());
	}

	@Test
	void getOrderById_shouldThrowIfMissing() {
		when(orderRepo.findById(200L)).thenReturn(Optional.empty());
		assertThrows(ResponseStatusException.class, () -> orderService.getOrderById(200L));
	}

	// --- markAsShipped / Delivered ---
	@Test
	void markAsShipped_shouldUpdateStatus() {
		Order order = new Order();
		order.setId(300L);
		order.setStatus(Status.PENDING);
		when(orderRepo.findById(300L)).thenReturn(Optional.of(order));

		orderService.markAsShipped(300L);

		assertEquals(Status.SHIPPED, order.getStatus());
		verify(orderRepo).save(order);
	}

	@Test
	void markAsDelivered_shouldUpdateStatus() {
		Order order = new Order();
		order.setId(400L);
		order.setStatus(Status.SHIPPED);
		when(orderRepo.findById(400L)).thenReturn(Optional.of(order));

		orderService.markAsDelivered(400L);

		assertEquals(Status.DELIVERED, order.getStatus());
		verify(orderRepo).save(order);
	}

	@Test
	void markAsShipped_shouldThrowIfMissing() {
		when(orderRepo.findById(300L)).thenReturn(Optional.empty());
		assertThrows(ResponseStatusException.class, () -> orderService.markAsShipped(300L));
	}

	@Test
	void markAsDelivered_shouldThrowIfMissing() {
		when(orderRepo.findById(400L)).thenReturn(Optional.empty());
		assertThrows(ResponseStatusException.class, () -> orderService.markAsDelivered(400L));
	}
}