package com.example.top.pages.service;

import com.example.top.pages.models.Category;
import com.example.top.pages.payload.response.ResponseEntityAppResponse;
import com.example.top.pages.repository.CategoryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ResponseEntityAppResponse responseEntityAppResponse;

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

    public ResponseEntity<?> deleteCategory(String categoryId) {
        Optional<Category> categoryCheck = categoryRepository.findByUUIDString(categoryId);
        if (categoryCheck.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Category not exists", categoryId);
        }
        Category category = categoryCheck.get();
        categoryRepository.delete(category);
        return responseEntityAppResponse.getAppResponse(HttpStatus.OK, "Category successfully delete", categoryId);
    }
}
