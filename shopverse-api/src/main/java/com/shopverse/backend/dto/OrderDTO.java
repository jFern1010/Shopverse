package com.shopverse.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.shopverse.backend.models.Status;

public class OrderDTO {

	private Long id;
	private LocalDateTime orderDate;
	private Status status;
	private double total;
	private List<OrderItemDTO> items;
	
	public OrderDTO(Long id, LocalDateTime orderDate, Status status, double total, List<OrderItemDTO> items) {
		super();
		this.id = id;
		this.orderDate = orderDate;
		this.status = status;
		this.total = total;
		this.items = items;
	}

	public OrderDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public List<OrderItemDTO> getItems() {
		return items;
	}

	public void setItems(List<OrderItemDTO> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "OrderDTO [id=" + id + ", orderDate=" + orderDate + ", status=" + status + ", total=" + total
				+ ", items=" + items + "]";
	}
	
	
}
