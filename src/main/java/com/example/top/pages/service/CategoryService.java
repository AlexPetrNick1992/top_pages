package com.example.top.pages.service;

import com.example.top.pages.models.Categories;
import com.example.top.pages.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Categories> getCategoryList() {
        return categoryRepository.findAll();
    }
}
