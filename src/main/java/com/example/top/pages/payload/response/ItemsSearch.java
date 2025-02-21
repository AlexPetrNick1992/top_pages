package com.example.top.pages.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemsSearch {
    String id;
    String name;
    String description;
}
