package com.shopverse.backend.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
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

import com.shopverse.backend.models.Cart;
import com.shopverse.backend.models.CartItem;
import com.shopverse.backend.models.Product;
import com.shopverse.backend.models.User;
import com.shopverse.backend.repositories.CartItemRepository;
import com.shopverse.backend.repositories.CartRepository;
import com.shopverse.backend.repositories.ProductRepository;
import com.shopverse.backend.repositories.UserRepository;
import com.shopverse.backend.services.CartService;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

	@Mock
	private ProductRepository productRepo;

	@Mock
	private CartRepository cartRepo;

	@Mock
	private UserRepository userRepo;

	@Mock
	private CartItemRepository cartItemRepo;

	@InjectMocks
	private CartService cartService;

	private User user;
	private Product product;

	@BeforeEach
	void setUp() {
		user = new User();
		user.setId(1L);

		product = new Product();
		product.setId(100L);
		product.setStock(10);

	}

	@Test
	void addToCart_shouldAddNewCartItem() {
		
		Cart cart = new Cart();
		cart.setId(1L);
		cart.setUser(user);
		cart.setItems(new ArrayList<>());
		when(userRepo.findById(1L)).thenReturn(Optional.of(user));
		when(productRepo.findById(100L)).thenReturn(Optional.of(product));
		when(cartRepo.findByUser(user)).thenReturn(Optional.of(cart));
		when(cartItemRepo.save(any(CartItem.class))).thenAnswer(inv -> inv.getArgument(0));
		when(productRepo.save(any(Product.class))).thenReturn(product);

		cartService.addToCart(1L, 100L, 2);

		verify(cartItemRepo, times(1)).save(any(CartItem.class));
		verify(productRepo, times(1)).save(any(Product.class));
		assertEquals(8, product.getStock());

	}

	@Test
	void addToCart_shouldUpdateExistingCartItem() {
		CartItem existingCartItem = new CartItem();
		existingCartItem.setProduct(product);
		existingCartItem.setQuantity(2);
		existingCartItem.setUser(user);

		Cart cart = new Cart();
		cart.setId(1L);
		cart.setUser(user);
		cart.setItems(new ArrayList<>(List.of(existingCartItem)));

		when(userRepo.findById(1L)).thenReturn(Optional.of(user));
		when(productRepo.findById(100L)).thenReturn(Optional.of(product));
		when(cartRepo.findByUser(user)).thenReturn(Optional.of(cart));
		when(cartItemRepo.save(any(CartItem.class))).thenAnswer(inv -> inv.getArgument(0));
		when(productRepo.save(any(Product.class))).thenReturn(product);

		cartService.addToCart(1L, 100L, 3);

		verify(cartItemRepo, times(1)).save(existingCartItem);
		verify(productRepo, times(1)).save(product);
		assertEquals(5, existingCartItem.getQuantity());
		assertEquals(7, product.getStock());
	}

	@Test
	void addToCart_shouldThrowInsufficientStock() {
		product.setStock(1);
		
		Cart cart = new Cart();
		cart.setId(1L);
		cart.setUser(user);
		cart.setItems(new ArrayList<>());
		
		when(userRepo.findById(1L)).thenReturn(Optional.of(user));
		when(productRepo.findById(100L)).thenReturn(Optional.of(product));
		when(cartRepo.findByUser(user)).thenReturn(Optional.of(cart));
		
		assertThrows(ResponseStatusException.class, () -> {
			cartService.addToCart(1L, 100L, 2);
		});
		
		verify(cartItemRepo, times(0)).save(any());
		verify(productRepo,times(0)).save(any());
		
	}
}
