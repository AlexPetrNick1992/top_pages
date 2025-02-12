package com.example.top.pages.payload.response;

import com.example.top.pages.models.Category;
import com.example.top.pages.models.Items;
import com.example.top.pages.repository.models.Items.ItemsCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ItemsListResponse {
    @JsonIgnore
    List<ItemsCategory> listItems;
    List<Map<String, Object>> items;

    public ItemsListResponse(List<ItemsCategory> listItems) {
        this.listItems = listItems;
        this.items = listItems.stream().map(
                items -> {
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put("id", String.valueOf(items.getItemId()));
                    m.put("name", items.getItemName());
                    m.put("description", items.getDescription());
                    m.put("is_approved", items.getIsapproved());
                    return m;
                }
        ).toList();
    }

}
