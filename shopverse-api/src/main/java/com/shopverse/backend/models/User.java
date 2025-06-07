package com.shopverse.backend.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "shop_user")
public class User {

	@Id
	@GeneratedValue
	private Long id;

	@Size(min = 5, message = "Username should be at least 5 letters")
	@Column(name = "user_name")
	private String userName;

	@Email
	private String email;

	private String password;

	private String address;

	private String phone;

	@OneToMany(mappedBy = "user")
	@JsonIgnore
	List<CartItem> cartItems = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	@JsonIgnore
	List<Order> orders = new ArrayList<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role")
	private Set<Role> roles = new HashSet<>();

	public User(Long id, String userName, String email, String password, String address, String phone,
			List<CartItem> cartItems, List<Order> orders, Set<Role> roles) {
		this.id = id;
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.address = address;
		this.phone = phone;
		this.cartItems = cartItems;
		this.orders = orders;
		this.roles = roles;
	}

	public User() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", email=" + email + ", password=" + password
				+ ", address=" + address + ", phone=" + phone + ", roles=" + roles + "]";
	}


}
