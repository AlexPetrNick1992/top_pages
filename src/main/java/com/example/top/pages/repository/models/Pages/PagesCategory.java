package com.example.top.pages.repository.models.Pages;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("isapproved")
    Boolean isApproved;
    @JsonProperty("category_id")
    String categoryId;
    @JsonProperty("name_category")
    String nameCategory;
    @JsonProperty("description")
    String description;
}
