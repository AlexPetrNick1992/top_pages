package com.example.top.pages.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemsRequest {
    @JsonProperty("items_name")
    @NotEmpty
    String itemsName;
    @JsonProperty("category_id")
    @NotEmpty
    String categoryId;
    String description;
}
