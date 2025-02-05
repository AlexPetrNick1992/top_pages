package com.example.top.pages.controller;


import com.example.top.pages.models.Pages;
import com.example.top.pages.service.PagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/pages")
public class PagesController {
    private final PagesService pagesService;

    @GetMapping
    public List<Pages> listPages() {

        return pagesService.getPagesList();
    }
}
