package com.example.top.pages.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemsRequests {
    String id;
    String name;
    String description;
    Integer status;
    @JsonProperty("created_at")
    String createdAt;
}
