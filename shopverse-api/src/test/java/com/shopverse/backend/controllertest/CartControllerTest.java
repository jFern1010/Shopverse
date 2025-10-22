package com.shopverse.backend.controllertest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopverse.backend.configuration.SecurityTestConfiguration;
import com.shopverse.backend.controllers.CartController;
import com.shopverse.backend.models.Cart;
import com.shopverse.backend.models.CartItem;
import com.shopverse.backend.models.Product;
import com.shopverse.backend.repositories.CartRepository;
import com.shopverse.backend.repositories.UserRepository;
import com.shopverse.backend.services.CartService;

@Import(SecurityTestConfiguration.class)
@WebMvcTest(CartController.class)
class CartControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CartService cartService;

	@MockBean
	private CartRepository cartRepo;

	@MockBean
	private UserRepository userRepo;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@WithMockUser(roles = "USER")
	void viewCart_shouldReturnCartItems() throws Exception {
		Product product = new Product();
		product.setId(100L);
		product.setTitle("Test Product");
		product.setPrice(50.0);

		CartItem item = new CartItem();
		item.setId(1L);
		item.setQuantity(2);
		item.setProduct(product);

		Cart cart = new Cart();
		cart.setId(1L);
		cart.setItems(List.of(item));

		when(cartRepo.findByUserId(1L)).thenReturn(Optional.of(cart));

		mockMvc.perform(MockMvcRequestBuilders.get("/shopverse/cart/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.items[0].productName").value("Test Product"))
				.andExpect(jsonPath("$.items[0].price").value(50.0)).andExpect(jsonPath("$.items[0].quantity").value(2))
				.andExpect(jsonPath("$.totalPrice").value(100.0));
	}

	@Test
	@WithMockUser(roles = "USER")
	void viewCart_shouldReturnEmptyCartJson() throws Exception {
		Cart cart = new Cart();
		cart.setId(1L);
		cart.setItems(List.of());

		when(cartRepo.findByUserId(1L)).thenReturn(Optional.of(cart));

		mockMvc.perform(MockMvcRequestBuilders.get("/shopverse/cart/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.items").isArray()).andExpect(jsonPath("$.items").isEmpty())
				.andExpect(jsonPath("$.totalPrice").value(0.0));
	}

	@Test
	@WithMockUser(roles = "USER")
	void viewCart_shouldReturnNotFoundIfUserMissing() throws Exception {
		when(cartRepo.findByUserId(1L)).thenReturn(Optional.empty());
		when(userRepo.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(MockMvcRequestBuilders.get("/shopverse/cart/1")).andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(roles = "USER")
	void addToCart_shouldCallServiceAndReturnSuccess() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/shopverse/cart/add/1/100/2")).andExpect(status().isOk())
				.andExpect(jsonPath("$").value("Item added to cart and stock updated"));

		verify(cartService, times(1)).addToCart(1L, 100L, 2);
	}

	@Test
	@WithMockUser(roles = "USER")
	void removeCartItem_shouldCallServiceAndReturnSuccess() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/shopverse/cart/remove/1/100")).andExpect(status().isOk())
				.andExpect(jsonPath("$").value("Item removed from cart and stock restored."));

		verify(cartService, times(1)).removeCartItem(1L, 100L);
	}
}