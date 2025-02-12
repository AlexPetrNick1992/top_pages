package com.example.top.pages.repository.models.Items;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemsCategory {
    @JsonProperty("item_id")
    String itemId;
    @JsonProperty("item_name")
    String itemName;
    String description;
    Boolean isapproved;
}
