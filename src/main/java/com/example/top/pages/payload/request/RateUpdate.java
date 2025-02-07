package com.example.top.pages.payload.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class RateUpdate {
    @NotEmpty(message = "This field is required")
    private String comment;
    private boolean isPositive;
}
