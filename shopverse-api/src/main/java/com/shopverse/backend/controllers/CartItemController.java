package com.shopverse.backend.controllers;

import com.shopverse.backend.repositories.ProductRepository;
import com.shopverse.backend.repositories.UserRepository;

public class CartItemController {

	private UserRepository userRepo;
	private ProductRepository productRepo;

	public CartItemController(UserRepository userRepo, ProductRepository productRepo) {
		this.userRepo = userRepo;
		this.productRepo = productRepo;
	}


}
