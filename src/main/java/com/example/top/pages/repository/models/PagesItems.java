package com.example.top.pages.repository.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagesItems {
    @JsonProperty("pages_id")
    String pagesId;
    @JsonProperty("items_id")
    String itemsId;
}
