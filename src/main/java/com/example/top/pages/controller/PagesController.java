package com.example.top.pages.controller;


import com.example.top.pages.models.Pages;
import com.example.top.pages.payload.request.PagesRequest;
import com.example.top.pages.service.PagesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/pages")
public class PagesController {
    private final PagesService pagesService;

    @GetMapping("/page")
    public ResponseEntity<?> getPagesById(
            @RequestParam(required = false) String pages_id,
            @RequestParam(required = false) String pages_name
    ) {
        return pagesService.getSinglePage(pages_id, pages_name);
    }

    @DeleteMapping
    public ResponseEntity<?> deletePages(@RequestParam(required = true, name = "pages_id") String pagesId) {
        return pagesService.deletePages(pagesId);
    }

    @GetMapping
    public List<Pages> listPages() {
        return pagesService.getPagesList();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPages(@Valid @RequestBody PagesRequest pagesRequest) {
        return pagesService.createPages(pagesRequest);
    }

    @GetMapping("/approve")
    public ResponseEntity<?> approvePages(@RequestParam(required = true, name = "pages_id") String pageId) {
        return pagesService.approvePages(pageId);
    }

    @GetMapping("/disprove")
    public ResponseEntity<?> disprovePages(@RequestParam(required = true, name = "pages_id") String pageId) {
        return pagesService.disprovePages(pageId);
    }
}
