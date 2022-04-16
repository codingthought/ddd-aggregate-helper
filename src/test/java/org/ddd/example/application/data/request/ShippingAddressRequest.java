package org.ddd.example.application.data.request;

import lombok.Data;

@Data
public class ShippingAddressRequest {
    private String receiverName;
    private String phone;
    private String address;
}
