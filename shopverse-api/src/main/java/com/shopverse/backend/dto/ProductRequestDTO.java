package com.shopverse.backend.dto;

public class ProductRequestDTO {

	public String title;
	public String description;
	public double price;
	public String imageUrl;
	public Long categoryId;

	public ProductRequestDTO(String title, String description, double price, String imageUrl, Long categoryId) {

		this.title = title;
		this.description = description;
		this.price = price;
		this.imageUrl = imageUrl;
		this.categoryId = categoryId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return "ProductRequestDTO [title=" + title + ", description=" + description + ", price=" + price + ", imageUrl="
				+ imageUrl + ", categoryId=" + categoryId + "]";
	}

}
