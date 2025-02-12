package com.example.top.pages.repository.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReturnCountRates {
    String id;
    String name;
    String description;
    Long count;
}
