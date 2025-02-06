package com.example.top.pages.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class RateUpdate {
    private String comment;
    private boolean isPositive;
}
