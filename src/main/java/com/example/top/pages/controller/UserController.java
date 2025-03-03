package com.example.top.pages.controller;

import com.example.top.pages.payload.request.ChangePassword;
import com.example.top.pages.payload.request.UserUpdateRequest;
import com.example.top.pages.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


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

    @PostMapping("api/v1/user/avatar")
    public ResponseEntity<?> loadAvatar(@RequestParam("file") MultipartFile file, @RequestParam("name_file") String nameFile) {
        return userService.loadAvatar(file, nameFile);
    }

//    @GetMapping("api/v1/user/avatar")
    @RequestMapping(path = "/api/v1/user/avatar", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getAvatar() throws IOException {
        return userService.getAvatar();
    }

}
