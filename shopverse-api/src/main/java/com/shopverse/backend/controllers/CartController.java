package com.shopverse.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopverse.backend.services.CartService;

@RestController
@RequestMapping("shopverse/cart")
public class CartController {

	private final CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}
	
	@PostMapping("/add")
	public ResponseEntity<String> addToCart(@RequestParam long userId, @RequestParam long productId,
			@RequestParam int quantity)
	{
		cartService.addToCart(userId, productId, quantity);
		return ResponseEntity.ok("Item added to cart and stock updated");

	}

	@DeleteMapping("/remove")
	public ResponseEntity<String> removeCartItem(@RequestParam long userId, @RequestParam long productId) {
		
		cartService.removeCartItem(userId, productId);

		return ResponseEntity.ok("Item removed from cart and stock restored.");
	}

}
