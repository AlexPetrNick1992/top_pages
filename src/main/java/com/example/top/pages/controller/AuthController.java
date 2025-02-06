package com.example.top.pages.controller;

import com.example.top.pages.payload.response.AppResponse;
import com.example.top.pages.payload.request.JwtTokenRequest;
import com.example.top.pages.payload.request.RegistrationUser;
import com.example.top.pages.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserServiceImpl userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("api/v1/auth")
    public ResponseEntity<?> authorization(@RequestBody JwtTokenRequest jwtTokenRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    jwtTokenRequest.getEmail(),
                    jwtTokenRequest.getPassword()
            ));
        } catch (
                BadCredentialsException e) {
            return new ResponseEntity<>(new AppResponse(HttpStatus.UNAUTHORIZED.value(), "Неправильный логин или пароль"), HttpStatus.UNAUTHORIZED);
        }
        return userService.authorization(jwtTokenRequest);
    }

    @PostMapping("api/v1/registration")
    public ResponseEntity<?> registration(@RequestBody RegistrationUser registrationUser) {
        return userService.registration(registrationUser);
    }

}
