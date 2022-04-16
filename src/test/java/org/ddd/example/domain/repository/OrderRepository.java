package org.ddd.example.domain.repository;

import org.ddd.example.domain.aggregate.Order;

public interface OrderRepository {
    Order save(Order order);
    Order get(String orderNo);
}
