package com.example.top.pages.controller;


import com.example.top.pages.models.Pages;
import com.example.top.pages.payload.request.PagesRequest;
import com.example.top.pages.payload.request.PagesUpdate;
import com.example.top.pages.service.PagesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/pages")
public class PagesController {
    private final PagesService pagesService;

    @GetMapping("/page")
    public ResponseEntity<?> getPagesById(
            @RequestParam(required = false) String pages_id,
            @RequestParam(required = false) String pages_title,
            @RequestParam(required = false) String mode,
            @RequestParam(required = false) String type
    ) {
        return pagesService.getSinglePage(pages_id, pages_title, mode, type);
    }

    @DeleteMapping
    public ResponseEntity<?> deletePages(@RequestParam(required = true, name = "pages_id") String pagesId) {
        return pagesService.deletePages(pagesId);
    }

    @GetMapping
    public ResponseEntity<?> listPages() {
        return pagesService.getPagesList();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPages(@Valid @RequestBody PagesRequest pagesRequest) {
        return pagesService.createPages(pagesRequest);
    }

    @PutMapping
    public ResponseEntity<?> updatePages(
            @RequestParam(required = true, name = "pages_id") String pagesId,
            @Valid @RequestBody PagesUpdate pagesUpdate
    ) {
        return pagesService.updatePages(pagesId, pagesUpdate);
    }

    @GetMapping("/join_item")
    public ResponseEntity<?> joinItem(
            @RequestParam(required = true, name = "item_id") String itemId,
            @RequestParam(required = true, name = "pages_id") String pagesId
    ) {
        return pagesService.joinItem(itemId, pagesId);
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
