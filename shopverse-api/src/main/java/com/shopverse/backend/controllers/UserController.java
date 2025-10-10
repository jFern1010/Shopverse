package com.shopverse.backend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import com.shopverse.backend.models.User;
import com.shopverse.backend.repositories.UserRepository;

import jakarta.validation.Valid;

//@RestController
//@RequestMapping("/shopverse/auth")
public class UserController {

	private final UserRepository userRepo;

	public UserController(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	@PostMapping("/register")
	public ResponseEntity<String> createUser(@Valid @RequestBody User user) {
		Optional<User> existingUser = userRepo.findByEmail(user.getEmail());

		if (existingUser.isPresent()) {
			return ResponseEntity.status(409).body("Email is already taken.");
		}
		userRepo.save(user);
		return ResponseEntity.ok("User registered successfully");
	}

	@GetMapping("/users")
			public List <User> getAllUsers() {
		return userRepo.findAll();
	}

	@GetMapping("/users/{id}")
	public User getUser(@PathVariable long id) {
		return userRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<Object> deleteUserById(@PathVariable long id) {
		User userToDelete = userRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

		boolean hasUndeliveredOrders = userToDelete.getOrders().stream()
				.anyMatch(order -> !"DELIVERED".equals(order.getStatus()));

		if (hasUndeliveredOrders) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has unhandled order/s");
		}
		userRepo.deleteById(id);

		return ResponseEntity.noContent().build();
	}
}
