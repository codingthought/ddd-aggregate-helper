package org.ddd.example.inforstructure.mapper;

import org.ddd.example.inforstructure.data.ItemFieldPO;

import java.util.List;

public interface ItemFieldMapper {
    static List<ItemFieldPO> findBy(Integer itemId) {
        return null;
    }

    ItemFieldPO[] save(ItemFieldPO[] itemFieldPO);
}
