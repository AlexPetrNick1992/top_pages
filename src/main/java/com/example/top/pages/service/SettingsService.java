package com.example.top.pages.service;

import com.example.top.pages.models.User;
import com.example.top.pages.payload.response.ItemsRequests;
import com.example.top.pages.payload.response.PagesRequestsUser;
import com.example.top.pages.payload.response.RateRequest;
import com.example.top.pages.repository.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
@Service
public class SettingsService {
    private final SettingsRepository settingsRepository;

    public String getDefaultAvatar() {
        return settingsRepository.findValueByName("default_avatar");
    }

}
