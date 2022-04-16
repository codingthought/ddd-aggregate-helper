package org.ddd.example.domain.aggregate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShippingAddress {
    private String receiverName;
    private String phone;
    private String address;
}
