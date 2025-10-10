package com.shopverse.backend.dto;

import com.shopverse.backend.models.Product;

public class ProductDTO {

	public Long id;
	public String title;
	public double price;
	public String description;
	public String category;
	public String imageUrl;
	public int stock;

	public ProductDTO(Product product) {
		this.id = product.getId();
		this.title = product.getTitle();
		this.price = product.getPrice();
		this.description = product.getDescription();
		this.category = product.getCategory().getName();
		this.imageUrl = product.getImageUrl();
		this.stock = product.getStock();
	}

	public ProductDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getImage() {
		return imageUrl;
	}

	public void setImage(String image) {
		this.imageUrl = image;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	@Override
	public String toString() {
		return "ProductDTO [id=" + id + ", title=" + title + ", price=" + price + ", description=" + description
				+ ", category=" + category + ", image=" + imageUrl + ", stock=" + stock + "]";
	}

}
