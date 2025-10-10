package com.shopverse.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.shopverse.backend.models.PaymentStatus;
import com.shopverse.backend.models.Status;

public class OrderDTO {

	private Long id;
	private UserDTO user;
	private LocalDateTime orderDate;
	private Status status;
	private double total;
	private List<OrderItemDTO> items;
	
	private String shippingName;
	private String shippingStreet;
	private String shippingCity;
	private String shippingZip;
	private String shippingCountry;

	private String billingName;
	private String billingStreet;
	private String billingCity;
	private String billingZip;
	private String billingCountry;

	private String paymentMethod;
	private PaymentStatus paymentStatus;
	private LocalDateTime paymentTimeStamp;

	public OrderDTO(Long id, UserDTO user, LocalDateTime orderDate, Status status, double total,
			List<OrderItemDTO> items,
			String shippingName, String shippingStreet, String shippingCity, String shippingZip, String shippingCountry,
			String billingName, String billingStreet, String billingCity, String billingZip, String billingCountry,
			String paymentMethod, PaymentStatus paymentStatus, LocalDateTime paymentTimeStamp) {
		this.id = id;
		this.user = user;
		this.orderDate = orderDate;
		this.status = status;
		this.total = total;
		this.items = items;
		this.shippingName = shippingName;
		this.shippingStreet = shippingStreet;
		this.shippingCity = shippingCity;
		this.shippingZip = shippingZip;
		this.shippingCountry = shippingCountry;
		this.billingName = billingName;
		this.billingStreet = billingStreet;
		this.billingCity = billingCity;
		this.billingZip = billingZip;
		this.billingCountry = billingCountry;
		this.paymentMethod = paymentMethod;
		this.paymentStatus = paymentStatus;
		this.paymentTimeStamp = paymentTimeStamp;
	}

	public OrderDTO() {

	}

	public Long getId() {
		return id;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
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

	public String getShippingName() {
		return shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}

	public String getShippingStreet() {
		return shippingStreet;
	}

	public void setShippingStreet(String shippingStreet) {
		this.shippingStreet = shippingStreet;
	}

	public String getShippingCity() {
		return shippingCity;
	}

	public void setShippingCity(String shippingCity) {
		this.shippingCity = shippingCity;
	}

	public String getShippingZip() {
		return shippingZip;
	}

	public void setShippingZip(String shippingZip) {
		this.shippingZip = shippingZip;
	}

	public String getShippingCountry() {
		return shippingCountry;
	}

	public void setShippingCountry(String shippingCountry) {
		this.shippingCountry = shippingCountry;
	}

	public String getBillingName() {
		return billingName;
	}

	public void setBillingName(String billingName) {
		this.billingName = billingName;
	}

	public String getBillingStreet() {
		return billingStreet;
	}

	public void setBillingStreet(String billingStreet) {
		this.billingStreet = billingStreet;
	}

	public String getBillingCity() {
		return billingCity;
	}

	public void setBillingCity(String billingCity) {
		this.billingCity = billingCity;
	}

	public String getBillingZip() {
		return billingZip;
	}

	public void setBillingZip(String billingZip) {
		this.billingZip = billingZip;
	}

	public String getBillingCountry() {
		return billingCountry;
	}

	public void setBillingCountry(String billingCountry) {
		this.billingCountry = billingCountry;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public LocalDateTime getPaymentTimeStamp() {
		return paymentTimeStamp;
	}

	public void setPaymentTimeStamp(LocalDateTime paymentTimeStamp) {
		this.paymentTimeStamp = paymentTimeStamp;
	}

	
	

}
