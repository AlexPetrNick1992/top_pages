package com.example.top.pages.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserUpdateRequest {
    @NotEmpty(message = "This field is required")
    String username;
    @NotEmpty(message = "This field is required")
    String email;
    @NotEmpty(message = "This field is required")
    String birthday;
}
