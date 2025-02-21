package com.example.top.pages.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PagesRequest {
    @NotEmpty(message = "This field is required")
    String name;
    String description;
    @NotEmpty(message = "This field is required")
    String category;
    @JsonProperty(value = "items")
    List<ItemInPagesCreate> itemsRequestList;

    public PagesRequest(String category, String name, List<ItemInPagesCreate> items) {
        this.category = category;
        this.name = name;
        this.itemsRequestList = items;
    }

    public PagesRequest(String name, String description, String category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public PagesRequest(String name, String category) {
        this.name = name;
        this.category = category;
    }
}
