package com.example.top.pages.controller;

import com.example.top.pages.service.ActionHistoryService;
import com.example.top.pages.service.impl.UserServiceImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/history")
@Data
public class HistoryController {
    private final ActionHistoryService actionHistoryService;
    private final UserServiceImpl userService;


    @GetMapping
    public ResponseEntity<?> getHistory() {
        return actionHistoryService.getUserHistory();
    }


}
