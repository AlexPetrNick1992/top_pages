package com.example.top.pages.service;

import com.example.top.pages.controller.UserController;
import com.example.top.pages.models.User;
import com.example.top.pages.repository.ItemsRepository;
import com.example.top.pages.repository.PagesRepository;
import com.example.top.pages.repository.RateRepository;
import com.example.top.pages.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@RequiredArgsConstructor
@Service
public class RequestService {
    private final UserRepository userRepository;
    private final RateRepository rateRepository;
    private final ItemsRepository itemsRepository;
    private final PagesRepository pagesRepository;

    public ResponseEntity<?> getAllRequestUser() {
        Authentication contextUser = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = contextUser.getAuthorities().stream().map(Object::toString).toList();
        User userItem = userRepository.findCheckedUserByEmail(contextUser.getPrincipal().toString());

        return ResponseEntity.ok("OK");
    }
}
