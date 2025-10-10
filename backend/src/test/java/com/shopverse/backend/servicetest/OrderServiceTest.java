package com.shopverse.backend.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
public class OrderServiceTest {

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

	@BeforeEach
	void setUp() {
		user = new User();
		user.setId(1L);
		
		product = new Product();
		product.setId(100L);
		product.setTitle("Test Title");
		product.setPrice(20.0);
		product.setImageUrl("image.jpg");
		product.setStock(5);
		
		cartItem = new CartItem();
		cartItem.setProduct(product);
		cartItem.setQuantity(2);
		
		cart = new Cart();
		cart.setUser(user);
		cart.setItems(new ArrayList<>(List.of(cartItem)));
		
	}

		@Test
		void placeOrder_success() {
			when(cartRepo.findByUserId(1L)).thenReturn(Optional.of(cart));
			
			OrderDTO result = orderService.placeOrder(1L);
			
			assertNotNull(result);
			assertEquals(1, result.getItems().size());
			assertEquals(40.0, result.getTotal());
			verify(orderRepo, times(1)).save(any(Order.class));
			verify(orderItemRepo, times(1)).saveAll(any());
			verify(cartRepo, times(1)).save(any(Cart.class));
			
		}

		@Test
		void placeOrder_shouldThrowIfCartMissing() {
			when(cartRepo.findByUserId(1L)).thenReturn(Optional.empty());

			assertThrows(Exception.class, () -> orderService.placeOrder(1L));
		}

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
		void processPayment_insufficientStock() {
			product.setStock(1);
			OrderItem item = new OrderItem();
			item.setProduct(product);
			item.setQuantity(2);

			Order order = new Order();
			order.setPaymentStatus(PaymentStatus.PENDING);
			order.setOrderItems(List.of(item));

			when(orderRepo.findById(100L)).thenReturn(Optional.of(order));

			assertThrows(Exception.class, () -> orderService.processPayment(100L));

		}

		@Test
		void getOrdersByUser_returnsMappedDTOs() {
			OrderItem item = new OrderItem();
			item.setId(1L);
			item.setProduct(product);
			item.setPrice(20.0);
			item.setQuantity(1);

			Order order = new Order();
			order.setId(99L);
			order.setOrderItems(List.of(item));
			order.setOrderDate(LocalDateTime.now());
			order.setStatus(Status.PENDING);
			order.setTotal(20.0);

			when(orderRepo.findByUserId(1L)).thenReturn(List.of(order));

			List<OrderDTO> orders = orderService.getOrdersByUser(1L);

			assertEquals(1, orders.size());
			assertEquals(20.0, orders.get(0).getTotal());
		}
}
