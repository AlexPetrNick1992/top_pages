package com.example.top.pages.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemInPagesCreate {
    @JsonProperty("name")
    @NotEmpty
    String itemsName;
    String description;
    String id;

    public ItemInPagesCreate(String name, String description) {
        this.itemsName = name;
        this.description = description;
    }

}
