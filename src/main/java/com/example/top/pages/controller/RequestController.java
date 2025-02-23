package com.example.top.pages.controller;

import com.example.top.pages.service.RequestService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/requests")
public class RequestController {
    private final RequestService requestService;

    @GetMapping
    public ResponseEntity<?> getHistory() {
        return requestService.getAllRequestUser();
    }

}
