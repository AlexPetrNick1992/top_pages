package com.example.top.pages.payload.response;

import com.example.top.pages.models.Pages;
import com.example.top.pages.repository.models.PagesItems;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PagesListResponse {
    @JsonIgnore
    Map<String, Object> mapPages;
    @JsonIgnore
    Map<String, Object> itemsCountInfo;
    @JsonIgnore
    List<PagesItems> pagesItemsList;
    String id;
    String name;
    String desc;
    /*
    id
    title
    desc
    category {
        id
        name
    }
    items ? [
        id
        name
        desc
        count_rates
    ]

    */

}
