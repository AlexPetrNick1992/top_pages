package com.example.top.pages.controller;

import com.example.top.pages.models.User;
import com.example.top.pages.payload.request.ChangePassword;
import com.example.top.pages.payload.request.PagesRequest;
import com.example.top.pages.payload.request.UserUpdateRequest;
import com.example.top.pages.repository.UserRepository;
import com.example.top.pages.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
public class UserController{

    private final UserServiceImpl userService;

    @GetMapping("api/v1/check_email")
    public ResponseEntity<?> checkEmail(@RequestParam(required = true, name = "email") String email) {
        return userService.checkEmail(email);
    }
    @GetMapping("api/v1/user/info")
    public ResponseEntity<?> getInfoUser() {
        return userService.getUserInfo();
    }

    @PutMapping("api/v1/user")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateRequest userUpdateInfo) {
        return userService.updateUser(userUpdateInfo);
    }

    @PutMapping("api/v1/user/change_password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePassword changePassword) {
        return userService.changePassword(changePassword);
    }

}
