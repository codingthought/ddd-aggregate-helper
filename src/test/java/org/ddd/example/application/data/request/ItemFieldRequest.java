package org.ddd.example.application.data.request;

import lombok.Data;

@Data
public class ItemFieldRequest {
    private String key;
    private String type;
    private String value;
}
