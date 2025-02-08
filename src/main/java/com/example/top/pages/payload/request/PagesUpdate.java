package com.example.top.pages.payload.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class PagesUpdate {
    String name;
    String description;
}
