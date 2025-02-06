package com.example.top.pages.service;

import com.example.top.pages.models.Category;
import com.example.top.pages.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getCategoryList() {
        return categoryRepository.findAll();
    }

    public Category getCategoryByUUID (UUID id) {
        Optional<Category> category = findCategoryByUUID(id);
        if (category.isEmpty()) {
            throw new IllegalStateException("Не найдена категория");
        } else {
            return category.get();
        }
    }

    public Optional<Category> findCategoryByUUID (UUID id) {
        return categoryRepository.findByUUID(id);
    }

    public Category createCategory(Category category) {
        Optional<Category> categoryFind = categoryRepository.findByUUID(category.getId());
        if (categoryFind.isPresent()) {
            throw new IllegalStateException("User has exists");
        }
        return categoryRepository.save(category);
    }
}
