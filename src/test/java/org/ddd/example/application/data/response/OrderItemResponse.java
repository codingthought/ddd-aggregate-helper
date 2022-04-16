package org.ddd.example.application.data.response;

import lombok.Data;

@Data
public class OrderItemResponse {
    private Integer id;
    private String name;
    private String skuId;
    private Integer price;
}
