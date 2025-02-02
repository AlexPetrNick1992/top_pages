package com.example.top.pages.service;

import com.example.top.pages.models.Category;
import com.example.top.pages.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getCategoryList() {
        return categoryRepository.findAll();
    }

    public Category getCategoryByUUID (String id) {
        Optional<Category> category = findCategoryByUUID(id);
        if (category.isEmpty()) {
            throw new IllegalStateException("Не найдена категория");
        } else {
            return category.get();
        }
    }

    public Optional<Category> findCategoryByUUID (String id) {
        return categoryRepository.findByUUID(id);
    }

    public Category createCategory(Category category) {
        Optional<Category> categoryFind = categoryRepository.findByUUID(category.getId());
        if (categoryFind.isPresent()) {
            System.out.println("Find Category");
            throw new IllegalStateException("User has exists");
        }
        System.out.println(category.toString());
        return categoryRepository.save(category);
    }
}
