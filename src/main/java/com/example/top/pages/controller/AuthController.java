package com.example.top.pages.controller;

import com.example.top.pages.exceptions.AppException;
import com.example.top.pages.models.User;
import com.example.top.pages.payload.request.JwtTokenRequest;
import com.example.top.pages.payload.request.RegistrationUser;
import com.example.top.pages.payload.response.JwtResponse;
import com.example.top.pages.payload.response.RegistrationResponse;
import com.example.top.pages.repository.RolesRepository;
import com.example.top.pages.service.impl.UserServiceImpl;
import com.example.top.pages.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserServiceImpl userService;
    private final RolesRepository rolesRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("api/v1/auth")
    public ResponseEntity<?> authorization(@RequestBody JwtTokenRequest jwtTokenRequest) {
        try {
            System.out.println(jwtTokenRequest.getEmail());
            System.out.println(jwtTokenRequest.getPassword());
            System.out.println(passwordEncoder.encode(jwtTokenRequest.getPassword()));
            System.out.println(userService.findByEmail(jwtTokenRequest.getEmail()));
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    jwtTokenRequest.getEmail(),
                    jwtTokenRequest.getPassword()
            ));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppException(HttpStatus.UNAUTHORIZED.value(), "Неправильный логин или пароль"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(jwtTokenRequest.getEmail());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("api/v1/registration")
    public ResponseEntity<?> registration(@RequestBody RegistrationUser registrationUser) {
        if (userService.findByEmail(registrationUser.getEmail()).isPresent()) {
            return new ResponseEntity<>(new AppException(HttpStatus.BAD_REQUEST.value(), "Пользователь с таким email уже создан"), HttpStatus.BAD_REQUEST);
        }
        User user = new User(
                registrationUser.getEmail(),
                passwordEncoder.encode(registrationUser.getPassword()),
                List.of(
                        rolesRepository.findByName("ROLE_USER")
                )
        );
        userService.createNewUser(user);
        return ResponseEntity.ok(new RegistrationResponse(HttpStatus.OK.value(),
                String.format("Пользователь %s зарегистрирован", registrationUser.getEmail())
        ));
    }

}
