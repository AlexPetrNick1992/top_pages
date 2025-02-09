package com.example.top.pages.controller;

import com.example.top.pages.models.User;
import com.example.top.pages.repository.UserRepository;
import com.example.top.pages.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
public class UserController{

    private final UserServiceImpl userService;

    @GetMapping("api/v1/user/info")
    public ResponseEntity<?> getInfoUser() {
        return userService.getUserInfo();
    }



}
