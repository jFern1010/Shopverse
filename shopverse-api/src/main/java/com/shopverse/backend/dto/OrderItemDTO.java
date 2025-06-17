package com.shopverse.backend.dto;

public class OrderItemDTO {

	private Long id;
	private String productName;
	private int quantity;
	private double price;
	private String imageUrl;

	public OrderItemDTO(Long id, String productName, int quantity, double price, String imageUrl) {
		this.id = id;
		this.productName = productName;
		this.quantity = quantity;
		this.price = price;
		this.imageUrl = imageUrl;
	}

	public OrderItemDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "OrderItemDTO [id=" + id + ", productName=" + productName + ", quantity=" + quantity + ", price=" + price
				+ ", imageUrl=" + imageUrl + "]";
	}

}