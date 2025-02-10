package com.example.top.pages.payload.response;

import com.example.top.pages.models.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class CategoryByPageReponse {
    @JsonIgnore
    Category category;
    String id;

    public CategoryByPageReponse(Category category) {
        this.id = String.valueOf(category.getId());
    }
}
