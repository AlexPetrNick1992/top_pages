package com.example.top.pages.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class RateAction {
    private String itemId;
    private String comment;

}
