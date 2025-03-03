package com.example.top.pages.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoadAvatar {
    @NotEmpty(message = "This field is required")
    @JsonProperty("avatar_image")
    String avatarImage;
}
