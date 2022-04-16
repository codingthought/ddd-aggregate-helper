package org.ddd.example.inforstructure.mapper;

import org.ddd.example.inforstructure.data.OrderItemPO;

import java.util.List;

public interface OrderItemMapper {
    OrderItemPO[] save(OrderItemPO[] orderItemPO);

    List<OrderItemPO> findBy(Integer orderId);
}
