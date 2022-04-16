package org.ddd.example.application.data.response;

import lombok.Data;

@Data
public class ItemFieldResponse {
    private Integer id;
    private String key;
    private String type;
    private String value;
}
