package org.ddd.example.domain.aggregate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemField {
    private Integer id;
    private String key;
    private String type;
    private String value;
}
