package com.example.top.pages.controller;

import com.example.top.pages.models.Category;
import com.example.top.pages.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getListCategory() {
        return this.categoryService.getCategoryList();
    }

    @GetMapping("/{id}")
    public Category getCategoryByUUID(@PathVariable UUID id) {
        return categoryService.getCategoryByUUID(id);
    }

    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }
}

