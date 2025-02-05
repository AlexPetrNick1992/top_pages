package com.example.top.pages.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class RegistrationUser {
    private final String email;
    private final String password;
    private final String confirmPassword;

}
