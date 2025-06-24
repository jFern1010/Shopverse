package com.shopverse.backend.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.shopverse.backend.dto.CartItemDTO;
import com.shopverse.backend.models.Cart;
import com.shopverse.backend.models.CartItem;
import com.shopverse.backend.repositories.CartRepository;
import com.shopverse.backend.services.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("shopverse/cart")
@Tag(name = "Cart", description = "Operations related to user's shopping cart")
public class CartController {

	private final CartService cartService;
	private final CartRepository cartRepo;

	public CartController(CartService cartService, CartRepository cartRepo) {
		this.cartService = cartService;
		this.cartRepo = cartRepo;
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/{userId}")
	@Operation(summary = "View cart", description = "Returns the contents and total price of a user's cart")
	public ResponseEntity<?> viewCart(@PathVariable long userId) {
		Cart cart = cartRepo.findByUserId(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));

		List<CartItem> cartItems = cart.getItems();

		if (cartItems.isEmpty()) {
			return ResponseEntity.ok("Cart is empty");
		}
		
		List<CartItemDTO> itemDTOs = cartItems.stream().map(item -> new CartItemDTO(item.getId(), item.getQuantity(),
				item.getProduct().getTitle(), item.getProduct().getPrice())).toList();

		double totalPrice = cartItems.stream().mapToDouble(x -> x.getProduct().getPrice() * x.getQuantity()).sum();

		Map<String, Object> response = new HashMap<>();
		response.put("items", itemDTOs);
		response.put("totalPrice", totalPrice);

		return ResponseEntity.ok(response);

	}

	@PreAuthorize("hasRole('USER')")
	@PostMapping("/add/{userId}/{productId}/{quantity}")
	@Operation(summary = "Add item to cart", description = "Adds a product to the user's cart and updates stock")
	public ResponseEntity<String> addToCart(@PathVariable long userId, @PathVariable long productId,
			@PathVariable int quantity)
	{
		cartService.addToCart(userId, productId, quantity);
		return ResponseEntity.ok("Item added to cart and stock updated");

	}

	@PreAuthorize("hasRole('USER')")
	@DeleteMapping("/remove/{userId}/{productId}")
	@Operation(summary = "Remove item from cart", description = "Removes a product from the user's cart and restores stock")
	public ResponseEntity<String> removeCartItem(@PathVariable long userId, @PathVariable long productId) {
		
		cartService.removeCartItem(userId, productId);

		return ResponseEntity.ok("Item removed from cart and stock restored.");
	}

}
