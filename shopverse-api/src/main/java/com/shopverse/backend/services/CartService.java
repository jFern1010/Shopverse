package com.shopverse.backend.services;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.shopverse.backend.models.Cart;
import com.shopverse.backend.models.CartItem;
import com.shopverse.backend.models.Product;
import com.shopverse.backend.models.User;
import com.shopverse.backend.repositories.CartItemRepository;
import com.shopverse.backend.repositories.CartRepository;
import com.shopverse.backend.repositories.ProductRepository;
import com.shopverse.backend.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CartService {

	private final CartRepository cartRepo;
	private final ProductRepository productRepo;
	private final UserRepository userRepo;
	private final CartItemRepository cartItemRepo;

	public CartService(CartRepository cartRepo, ProductRepository productRepo, UserRepository userRepo,
			CartItemRepository cartItemRepo) {
		this.cartRepo = cartRepo;
		this.productRepo = productRepo;
		this.userRepo = userRepo;
		this.cartItemRepo = cartItemRepo;
	}

	@Transactional
	public void addToCart(Long userId, Long productId, int quantity) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

		Cart cart = cartRepo.findByUser(user).orElseGet(() -> {
			Cart newCart = new Cart();
			newCart.setUser(user);
			return cartRepo.save(newCart);
		});

		Optional<CartItem> cartItem = cart.getItems().stream().filter(x -> x.getId().equals(productId)).findFirst();

		if (cartItem.isPresent()) {
			CartItem existingCartItem = cartItem.get();
			int newQuantity = existingCartItem.getQuantity() + quantity;

			if (product.getStock() < quantity) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock");
			}

			existingCartItem.setQuantity(newQuantity);
			cartItemRepo.save(existingCartItem);
		} else {
			if (product.getStock() < quantity) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock");
			}


		CartItem newCartItems = new CartItem();
		newCartItems.setCart(cart);
		newCartItems.setProduct(product);
		newCartItems.setUser(user);
		newCartItems.setQuantity(quantity);
		cartItemRepo.save(newCartItems);

	}

		product.setStock(product.getStock() - quantity);
		productRepo.save(product);

	}

	@Transactional
	public void removeCartItem(Long userId, Long productId) {

		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

		Cart cart = cartRepo.findByUser(user)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, " Cart not found"));

		CartItem cartItemToRemove = cart.getItems().stream().filter(x -> x.getId().equals(productId)).findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));

		Product productToRemove = cartItemToRemove.getProduct();

		productToRemove.setStock(product.getStock() + cartItemToRemove.getQuantity());
		productRepo.save(productToRemove);
		cartItemRepo.delete(cartItemToRemove);
	}

}
