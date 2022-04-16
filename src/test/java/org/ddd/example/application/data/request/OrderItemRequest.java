package org.ddd.example.application.data.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderItemRequest {
    private String name;
    private String skuId;
    private Integer price;
    private List<ItemFieldRequest> fields = List.of();
    private String remark;
}
