package org.ddd.example.inforstructure.repository;

import org.ddd.example.domain.aggregate.ItemField;
import org.ddd.example.domain.aggregate.Order;
import org.ddd.example.domain.aggregate.OrderItem;
import org.ddd.example.domain.aggregate.ShippingAddress;
import org.ddd.example.domain.repository.OrderRepository;
import org.ddd.example.inforstructure.data.ItemFieldPO;
import org.ddd.example.inforstructure.data.OrderItemPO;
import org.ddd.example.inforstructure.data.OrderPO;
import org.ddd.example.inforstructure.mapper.ItemFieldMapper;
import org.ddd.example.inforstructure.mapper.OrderItemMapper;
import org.ddd.example.inforstructure.mapper.OrderMapper;
import org.ddd.helper.Suppliers;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRepositoryImpl implements OrderRepository {

    private OrderMapper orderMapper;
    private OrderItemMapper orderItemMapper;
    private ItemFieldMapper itemFieldMapper;

    @Override
    public Order save(Order order) {
        OrderPO orderPO = toPO(order);
        orderMapper.save(orderPO);
        if (order.getItems().isHere()) {
            OrderItemPO[] items = order.getItems().get().stream().map(this::toPO).toArray(OrderItemPO[]::new);
            orderItemMapper.save(items);
            for (OrderItem item : order.getItems().get()) {
                if (item.getItemFields().isHere() && !item.getItemFields().get().isEmpty()) {
                    ItemFieldPO[] fields = item.getItemFields().get().stream().map(this::toPO).toArray(ItemFieldPO[]::new);
                    itemFieldMapper.save(fields);
                }
            }
        }
        return get(order.getNo());
    }

    private ItemFieldPO toPO(ItemField itemField) {
        return new ItemFieldPO();
    }

    private OrderItemPO toPO(OrderItem orderItem) {
        return new OrderItemPO();
    }

    private OrderPO toPO(Order order) {
        return new OrderPO();
    }

    @Override
    public Order get(String orderNo) {
        OrderPO orderPO = orderMapper.get(orderNo);
        if (orderPO == null) {
            throw new RuntimeException(String.format("order not found orderNo:%s", orderNo));
        }
        Integer orderId = orderPO.getId();
        return new Order(orderId, orderPO.getNo(), orderPO.getId(),
                Suppliers.awareMemoize(() -> toDOs(orderItemMapper.findBy(orderId))),
                Suppliers.self(toSippingAddressDO(orderPO)));
    }

    private ShippingAddress toSippingAddressDO(OrderPO orderPO) {
        return ShippingAddress.builder()
                .address(orderPO.getAddress())
                .receiverName(orderPO.getReceiverName())
                .phone(orderPO.getPhone())
                .build();
    }

    private List<OrderItem> toDOs(List<OrderItemPO> itemPOS) {
        return itemPOS.stream().map(itemPO -> {
            Integer itemId = itemPO.getId();
            return OrderItem.builder().id(itemId)
                    .itemFields(Suppliers.awareMemoize(() -> this.toItemFileds(ItemFieldMapper.findBy(itemId))))
                    .build();
        }).collect(Collectors.toList());
    }

    private List<ItemField> toItemFileds(List<ItemFieldPO> itemFieldPOS) {
        return null;
    }
}
