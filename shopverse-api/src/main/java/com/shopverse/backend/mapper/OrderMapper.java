package com.shopverse.backend.mapper;

import com.shopverse.backend.dto.OrderDTO;
import com.shopverse.backend.dto.OrderItemDTO;
import com.shopverse.backend.models.Order;

public class OrderMapper {

	public static OrderDTO toDTO(Order order) {
		OrderDTO dto = new OrderDTO();
		dto.setId(order.getId());
		dto.setOrderDate(order.getOrderDate());
		dto.setStatus(order.getStatus());
		dto.setTotal(order.getTotal());
		dto.setUser(UserMapper.toDTO(order.getUser()));

		dto.setShippingName(order.getShippingName());
		dto.setShippingStreet(order.getShippingStreet());
		dto.setShippingCity(order.getShippingCity());
		dto.setShippingZip(order.getShippingZip());
		dto.setShippingCountry(order.getShippingCountry());

		dto.setBillingName(order.getBillingName());
		dto.setBillingStreet(order.getBillingStreet());
		dto.setBillingCity(order.getBillingCity());
		dto.setBillingZip(order.getBillingZip());
		dto.setBillingCountry(order.getBillingCountry());

		dto.setPaymentMethod(order.getPaymentMethod());
		dto.setPaymentStatus(order.getPaymentStatus());
		dto.setPaymentTimeStamp(order.getPaymentTimeStamp());

		dto.setItems(order.getOrderItems().stream().map(OrderItemDTO::fromEntity).toList());

		return dto;

	}
}
