package com.example.top.pages.payload.response;

import com.example.top.pages.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
public class UserInfoResponse {
    @JsonIgnore
    User user;
    String id;
    String username;
    String email;
    String birthday;
    @JsonProperty("count_rates")
    int countRates;

    public UserInfoResponse(User user, int countRates) {
        this.id = String.valueOf(user.getId());
        this.username = user.getName();
        this.email = user.getEmail();
        this.birthday = String.valueOf(user.getDateOfBirth());
        this.countRates = countRates;
    }
}
