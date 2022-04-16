package org.ddd.example.domain.factory;

import org.ddd.example.domain.aggregate.Order;
import org.ddd.example.domain.aggregate.OrderItem;
import org.ddd.example.domain.aggregate.ShippingAddress;

import java.util.List;

import static org.ddd.helper.Suppliers.self;

public interface OrderFactory {
    default Order crateOrder(String no) {
        return new Order(no, self(getOrderItems()), self(getShippingAddress()));
    }
    List<OrderItem> getOrderItems();
    ShippingAddress getShippingAddress();
}
