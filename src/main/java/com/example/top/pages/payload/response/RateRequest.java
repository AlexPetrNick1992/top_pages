package com.example.top.pages.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/*
* r.id as rate_id, r."comment" as rate_comment, r.ispositive, r.status, r.category_id, r.created_at
* */

@Data
@AllArgsConstructor
public class RateRequest {
    @JsonProperty("rate_id")
    String id;
    @JsonProperty("rate_comment")
    String comment;
    @JsonProperty("ispositive")
    Boolean isPositive;
    Integer status;
    @JsonProperty("category_id")
    String categoryId;
    @JsonProperty("created_at")
    String createdAt;
}
