package com.shopverse.backend.dto;

public class CartItemDTO {

	private Long id;
	private int quantity;
	private String productName;
	private double price;

	public CartItemDTO(Long id, int quantity, String productName, double price) {
		this.id = id;
		this.quantity = quantity;
		this.productName = productName;
		this.price = price;
	}

	public CartItemDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "CartItemDTO [id=" + id + ", quantity=" + quantity + ", productName=" + productName + ", price=" + price
				+ "]";
	}

}
