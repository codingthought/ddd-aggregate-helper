package org.ddd.example.application.data.response;

import lombok.Data;

import java.util.List;

@Data
public class OrderResponse {

    private Integer id;
    private String no;
    private Integer totalPrice;
    private List<OrderItemResponse> items;
    private ShippingAddressResponse shippingAddress;
}
