package com.example.top.pages.payload.response;

import com.example.top.pages.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    @JsonIgnore
    User user;
    String id;
    String username;
    String email;
    String birthday;
    @JsonProperty("count_rates")
    long countRates;
    @JsonProperty(value = "created_at")
    private String createdAt;

    public UserInfoResponse(String id, String username, String email, String birthday, String createdAt, long countRates) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.birthday = birthday;
        this.countRates = countRates;
        this.createdAt = createdAt;
    }

    public UserInfoResponse(User user, int countRates, String createdAt) {
        this.id = String.valueOf(user.getId());
        this.username = user.getName();
        this.email = user.getEmail();
        this.birthday = String.valueOf(user.getDateOfBirth());
        this.countRates = countRates;
        this.createdAt = createdAt;
    }
}
