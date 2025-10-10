package com.shopverse.backend.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Category {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@JsonIgnore
	@OneToMany(mappedBy = "category")
	private List<Product> products = new ArrayList<>();

	public Category(Long id, String name, List<Product> products) {
		this.id = id;
		this.name = name;
		this.products = products;
	}

	public Category(String name) {
		this.name = name;
	}

	public Category() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Product> getProducts() {
		return products;
	}


	public void setProduct(List<Product> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", products=" + products + "]";
	}

}
