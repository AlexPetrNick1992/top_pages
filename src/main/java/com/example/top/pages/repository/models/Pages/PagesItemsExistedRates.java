package com.example.top.pages.repository.models.Pages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagesItemsExistedRates {
    @JsonProperty(value = "item_id")
    String itemId;
    Long count;
}
