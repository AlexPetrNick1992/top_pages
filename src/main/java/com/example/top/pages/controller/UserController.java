package com.example.top.pages.controller;

import com.example.top.pages.models.User;
import com.example.top.pages.repository.UserRepository;
import com.example.top.pages.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Data
@AllArgsConstructor
@RestController
public class UserController{

    private final UserRepository userRepository;

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

}
