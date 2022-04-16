package org.ddd.example.inforstructure.mapper;

import org.ddd.example.inforstructure.data.OrderPO;

public interface OrderMapper {
    OrderPO save(OrderPO order);

    OrderPO get(String orderNo);
}
