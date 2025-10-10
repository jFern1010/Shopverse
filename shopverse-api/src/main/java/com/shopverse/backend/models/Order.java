package com.shopverse.backend.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "purchase_order")
public class Order {

	@Id
	@GeneratedValue
	private Long id;

	private LocalDateTime orderDate;

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

	@Column(nullable = false)
	private String paymentMethod;

	@Enumerated(EnumType.STRING)
	private Status status;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<>();

	@Column(nullable = false)
	private double total;

	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus = PaymentStatus.PENDING;

	private LocalDateTime paymentTimeStamp;

	public Order(Long id, LocalDateTime orderDate, String shippingName, String shippingStreet, String shippingCity,
			String shippingZip, String shippingCountry, String billingName, String billingStreet, String billingCity,
			String billingZip, String billingCountry, String paymentMethod, Status status, User user,
			List<OrderItem> orderItems, double total, PaymentStatus paymentStatus, LocalDateTime paymentTimeStamp) {
		this.id = id;
		this.orderDate = orderDate;
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
		this.status = status;
		this.user = user;
		this.orderItems = orderItems;
		this.total = total;
		this.paymentStatus = paymentStatus;
		this.paymentTimeStamp = paymentTimeStamp;
	}

	public Order() {
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
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
