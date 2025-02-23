package com.example.top.pages.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagesRequestsUser {
    String id;
    String title;
    String description;
    Integer status;
    String created_at;
}
