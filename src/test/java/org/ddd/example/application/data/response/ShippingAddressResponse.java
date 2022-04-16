package org.ddd.example.application.data.response;

import lombok.Data;

@Data
public class ShippingAddressResponse {
    private String receiverName;
    private String phone;
    private String address;
}
