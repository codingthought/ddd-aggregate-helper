package org.ddd.example.domain.aggregate;

import lombok.Builder;
import lombok.Getter;
import org.ddd.helper.As;

import java.util.List;

@Builder
@Getter
public class OrderItem {
    private Integer id;
    private String name;
    private String skuId;
    private Integer price;
    private As<List<ItemField>> itemFields;
    private String remark;

    public void updateRemark(String remark) {
        this.remark = remark;
    }
}

