package com.example.top.pages.payload.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
public class AppResponse {
    private final int status;
    private final String message;
    private Date timestamp;
    private String id;

    public AppResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
