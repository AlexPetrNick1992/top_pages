package com.example.top.pages.payload.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemsRequest {
    @NotEmpty
    String itemsName;
    @NotEmpty
    String categoryId;
    String description;
}
