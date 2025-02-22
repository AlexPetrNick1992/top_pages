package com.example.top.pages.service;

import com.example.top.pages.models.Category;
import com.example.top.pages.models.User;
import com.example.top.pages.payload.response.CategoryByPageReponse;
import com.example.top.pages.payload.response.ResponseEntityAppResponse;
import com.example.top.pages.repository.CategoryRepository;
import com.example.top.pages.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final ActionHistoryService actionHistoryService;
    private final UserRepository userRepository;

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
        Authentication contextUser = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = contextUser.getAuthorities().stream().map(Object::toString).toList();
        User userItem = userRepository.findCheckedUserByEmail(contextUser.getPrincipal().toString());

        Optional<Category> categoryCheck = categoryRepository.findByUUIDString(categoryId);
        if (categoryCheck.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Category not exists", categoryId);
        }
        Category category = categoryCheck.get();
        categoryRepository.delete(category);
        if (roles.contains("ROLE_ADMIN")) {
            actionHistoryService.createHistoryByTypeWithAdmin(
                    ActionHistoryService.TypeHistoryAdmin.DELETED_CATEGORY, userItem.getName(),
                    category.getName(), category.getCreatedBy()
            );
        } else {
            actionHistoryService.createHistoryByType(
                    ActionHistoryService.TypeHistory.DELETED_CATEGORY, userItem, category.getName()
            );
        }
        return responseEntityAppResponse.getAppResponse(HttpStatus.OK, "Category successfully delete", categoryId);
    }

    public ResponseEntity<?> getCategoryByPage(String pagesId) {
        Category category = categoryRepository.getCategoryByIdPages(pagesId);
        return ResponseEntity.ok(new CategoryByPageReponse(category));
    }
}
