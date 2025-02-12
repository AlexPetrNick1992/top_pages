package com.example.top.pages.payload.response;

import com.example.top.pages.models.Pages;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ResponseSinglePages {
    Pages pages;
    int count_items;
    String name;
//    String itemName;
//    String itemComment;
//    String ratesCount;
    List<Map<String, String>> items;

    public ResponseSinglePages(Pages pages) {
        this.name = pages.getTitle();
        this.count_items = pages.getItems().size();
        this.items = pages.getItems().stream().map(
                items1 -> {
                    Map<String, String> m = new HashMap<String, String>();
                    m.put("itemName", items1.getName());
                    m.put("itemComment", items1.getDescription());
                    m.put("ratesCount", String.valueOf(items1.getRate().size()));
                    return m;
                }
        ).toList();
    }
}
