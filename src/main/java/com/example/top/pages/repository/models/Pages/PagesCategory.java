package com.example.top.pages.repository.models.Pages;

import com.example.top.pages.models.converter.StatusEntityConverter;
import com.example.top.pages.service.enumerate.StatusEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagesCategory {
    @JsonProperty("pages_id")
    String pagesId;
    @JsonProperty("title")
    String title;
    @JsonProperty("description_pages")
    String descriptionPages;
    @Convert(converter = StatusEntityConverter.class)
    Integer status;
    @JsonProperty("category_id")
    String categoryId;
    @JsonProperty("name_category")
    String nameCategory;
    @JsonProperty("description")
    String description;
}
