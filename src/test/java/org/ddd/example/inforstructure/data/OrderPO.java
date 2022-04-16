package org.ddd.example.inforstructure.data;

import lombok.Data;

@Data
public class OrderPO {
    public String no;
    private Integer id;
    private Integer totalPrice;
    private String receiverName;
    private String phone;
    private String address;
    private String remark;
}
