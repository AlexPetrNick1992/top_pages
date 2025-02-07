package com.example.top.pages.payload.response;

import com.example.top.pages.models.Pages;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ResponseSinglePages {
    Pages pages;
    int count;

    public ResponseSinglePages(Pages pages) {
        this.pages = pages;
        this.count = 5;
    }
}
