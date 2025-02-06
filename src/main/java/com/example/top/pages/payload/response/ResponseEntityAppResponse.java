package com.example.top.pages.payload.response;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Configuration
public class ResponseEntityAppResponse {

    public ResponseEntity<?> getAppResponse(HttpStatus status, String message, String idCreated) {
        AppResponse response = new AppResponse(status.value(), message);
        if (idCreated != null) {response.setId(idCreated);}
        return new ResponseEntity<>(response, status);
    }
}
