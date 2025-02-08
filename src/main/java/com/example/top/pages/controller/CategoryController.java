package com.example.top.pages.controller;

import com.example.top.pages.models.Category;
import com.example.top.pages.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(path="api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<Category> getListCategory() {
        return this.categoryService.getCategoryList();
    }

    @GetMapping("/{id}")
    public Category getCategoryByUUID(@PathVariable UUID id) {
        return categoryService.getCategoryByUUID(id);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCategory (@RequestParam(required = true, name = "category_id") String categoryId) {
        return categoryService.deleteCategory(categoryId);
    }

    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }
}

