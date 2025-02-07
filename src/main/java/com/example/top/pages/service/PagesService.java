package com.example.top.pages.service;

import com.example.top.pages.models.Category;
import com.example.top.pages.models.Pages;
import com.example.top.pages.payload.response.ResponseEntityAppResponse;
import com.example.top.pages.payload.response.ResponseSinglePages;
import com.example.top.pages.repository.CategoryRepository;
import com.example.top.pages.repository.PagesRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PagesService {

    private final PagesRepository pagesRepository;
    private final ResponseEntityAppResponse responseEntityAppResponse;

    public List<Pages> getPagesList() {return pagesRepository.findAll();}

    public ResponseEntity<?> getPageByUUID (String id) {
        Optional<Pages> pages = pagesRepository.findByUUIDString(id);
        if (pages.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, String.format("Pages %s not exists", id), null);
        } else {
            return ResponseEntity.ok(new ResponseSinglePages(pages.get()));
        }
    }

}
