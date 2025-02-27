package com.example.top.pages.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ChangePassword {
    @JsonProperty("new_password")
    @NotEmpty(message = "This field is required")
    String newPassword;
    @JsonProperty("confirm_password")
    @NotEmpty(message = "This field is required")
    String confirmPassword;
}
