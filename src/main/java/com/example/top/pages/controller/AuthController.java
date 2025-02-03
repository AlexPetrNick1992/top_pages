package com.example.top.pages.controller;

import com.example.top.pages.service.impl.UserServiceImpl;
import com.example.top.pages.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

//@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserServiceImpl userService;
    private final JwtTokenUtils jwtTokenUtils;

}
