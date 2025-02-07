package com.example.top.pages.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RateAction {
    @JsonProperty("item_id")
    @NotEmpty(message = "Field item_id is required")
    private String itemId;
    @NotEmpty(message = "Field comment is required")
    private String comment;
    @NotEmpty(message = "Field comment is required")
    private String categoryId;
    private Boolean isPositive;
}
