package com.example.top.pages.payload.response;

import com.example.top.pages.models.Category;
import com.example.top.pages.models.Items;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ItemsListResponse {
    @JsonIgnore
    List<Items> listItems;
    List<Map<String, Object>> items;

    public ItemsListResponse(List<Items> listItems) {
        this.listItems = listItems;
        this.items = listItems.stream().map(
                items -> {
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put("id", String.valueOf(items.getId()));
                    m.put("name", items.getName());
                    m.put("description", items.getDescription());
                    m.put("is_approved", items.getIsApproved());
                    m.put("category_id", items.getCategory().stream().map(category -> String.valueOf(category.getId())).toList());
                    return m;
                }
        ).toList();
    }

}
