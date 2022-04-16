package org.ddd.example.application.data.request;

import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class OrderRequest {
    private Optional<String> no = Optional.empty();
    private List<OrderItemRequest> items = List.of();
    private ShippingAddressRequest shippingAddress;
    private String remark;
}
