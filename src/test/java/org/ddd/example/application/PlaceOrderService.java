package org.ddd.example.application;

import org.ddd.example.application.data.request.OrderRequest;
import org.ddd.example.application.data.response.OrderResponse;
import org.ddd.example.application.factory.OrderFactoryImpl;
import org.ddd.example.domain.aggregate.Order;
import org.ddd.example.domain.repository.OrderRepository;

public class PlaceOrderService {

    private OrderRepository orderRepository;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        Order order = OrderFactoryImpl.of(orderRequest).crateOrder();
        return toResponse(orderRepository.save(order));
    }

    public OrderResponse updateRemark(String orderNo, String remark) {
        Order order = orderRepository.get(orderNo);
        order.replaceRemark(remark);
        return toResponse(orderRepository.save(order));
    }


    public OrderResponse updateRemark(String orderNo, Integer itemId, String remark) {
        Order order = orderRepository.get(orderNo);
        order.replaceRemark(itemId, remark);
        return toResponse(orderRepository.save(order));
    }

    private OrderResponse toResponse(Order crateOrder) {
        return null;
    }
}
