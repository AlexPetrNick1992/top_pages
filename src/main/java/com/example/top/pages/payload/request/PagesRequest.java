package com.example.top.pages.payload.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class PagesRequest {
    @NotEmpty(message = "This field is required")
    String name;
    String description;
    @NotEmpty(message = "This field is required")
    String category;
}
