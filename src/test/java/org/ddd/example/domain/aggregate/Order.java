package org.ddd.example.domain.aggregate;

import lombok.Getter;
import org.ddd.helper.As;

import java.util.List;


@Getter
public class Order {
    public Order(String no, As<List<OrderItem>> items, As<ShippingAddress> shippingAddress) {
        this.no = no;
        this.totalPrice = items.get().stream().mapToInt(OrderItem::getPrice).sum();
        this.items = items;
        this.shippingAddress = shippingAddress;
    }

    public Order(Integer id, String no, Integer totalPrice, As<List<OrderItem>> items, As<ShippingAddress> shippingAddress) {
        this.id = id;
        this.no = no;
        this.totalPrice = totalPrice;
        this.items = items;
        this.shippingAddress = shippingAddress;
    }

    private Integer id;
    private String no;
    private Integer totalPrice;
    private As<List<OrderItem>> items;
    private As<ShippingAddress> shippingAddress;
    private String remark;

    public void replaceRemark(String remark) {
        this.remark = remark;
        // log update
    }

    public void replaceRemark(Integer itemId, String remark) {
        this.getItems().get().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst().ifPresent(item -> item.updateRemark(remark));
    }
}
