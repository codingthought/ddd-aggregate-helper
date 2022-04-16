package org.ddd.example.application.factory;

import org.ddd.example.application.data.request.ItemFieldRequest;
import org.ddd.example.application.data.request.OrderItemRequest;
import org.ddd.example.application.data.request.OrderRequest;
import org.ddd.example.application.data.request.ShippingAddressRequest;
import org.ddd.example.domain.aggregate.ItemField;
import org.ddd.example.domain.aggregate.Order;
import org.ddd.example.domain.aggregate.OrderItem;
import org.ddd.example.domain.aggregate.ShippingAddress;
import org.ddd.example.domain.factory.OrderFactory;
import org.ddd.helper.Suppliers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderFactoryImpl implements OrderFactory {

    private final OrderRequest orderDTO;

    private OrderFactoryImpl(OrderRequest orderRequest) {
        this.orderDTO = orderRequest;
    }

    public static OrderFactoryImpl of(OrderRequest orderDTO) {
        return new OrderFactoryImpl(orderDTO);
    }

    public Order crateOrder() {
        return crateOrder(orderDTO.getNo().orElseGet(this::genOrderNo));
    }

    private String genOrderNo() {
        return UUID.randomUUID().toString();
    }

    @Override
    public List<OrderItem> getOrderItems() {
        return orderDTO.getItems().stream().map(this::toItem).collect(Collectors.toList());
    }

    private OrderItem toItem(OrderItemRequest orderItemRequest) {
        return OrderItem.builder().name(orderItemRequest.getName())
                .price(orderItemRequest.getPrice())
                .skuId(orderItemRequest.getSkuId())
                .itemFields(Suppliers.self(toDO(orderItemRequest.getFields())))
                .build();
    }

    private List<ItemField> toDO(List<ItemFieldRequest> fields) {
        return fields.stream().map(filed -> ItemField.builder()
                        .key(filed.getKey()).type(filed.getType()).value(filed.getValue()).build())
                .collect(Collectors.toList());
    }

    @Override
    public ShippingAddress getShippingAddress() {
        ShippingAddressRequest dto = orderDTO.getShippingAddress();
        return ShippingAddress.builder()
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .receiverName(dto.getReceiverName())
                .build();
    }
}
