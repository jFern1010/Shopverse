package com.shopverse.backend.dto;

public class CheckoutRequestDTO {

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

	public CheckoutRequestDTO(String shippingName, String shippingStreet, String shippingCity, String shippingZip,
			String shippingCountry, String billingName, String billingStreet, String billingCity, String billingZip,
			String billingCountry, String paymentMethod) {
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
	}

	public CheckoutRequestDTO() {

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

	@Override
	public String toString() {
		return "CheckoutRequestDTO [shippingName=" + shippingName + ", shippingStreet=" + shippingStreet
				+ ", shippingCity=" + shippingCity + ", shippingZip=" + shippingZip + ", shippingCountry="
				+ shippingCountry + ", billingName=" + billingName + ", billingStreet=" + billingStreet
				+ ", billingCity=" + billingCity + ", billingZip=" + billingZip + ", billingCountry=" + billingCountry
				+ ", paymentMethod=" + paymentMethod + "]";
	}


}
