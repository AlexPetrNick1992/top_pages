package com.example.top.pages.service;

import com.example.top.pages.models.Category;
import com.example.top.pages.models.Items;
import com.example.top.pages.models.Pages;
import com.example.top.pages.payload.request.ItemsRequest;
import com.example.top.pages.payload.response.ItemsListResponse;
import com.example.top.pages.payload.response.ResponseEntityAppResponse;
import com.example.top.pages.repository.CategoryRepository;
import com.example.top.pages.repository.ItemsRepository;
import com.example.top.pages.repository.PagesRepository;
import com.example.top.pages.repository.models.Items.ItemsCategory;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@Service
public class ItemsService {
    private final ItemsRepository itemsRepository;
    private final CategoryRepository categoryRepository;
    private final ResponseEntityAppResponse responseEntityAppResponse;
    private final PagesRepository pagesRepository;

    public ResponseEntity<?> getItemsList(String categoryId) {
        List<ItemsCategory> itemsList = itemsRepository.getItemsByCategory(categoryId);
        return ResponseEntity.ok(new ItemsListResponse(itemsList));
    }

    public ResponseEntity<?> createItems(ItemsRequest itemsRequest) {
        Authentication contextUser = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = contextUser.getAuthorities().stream().map(Object::toString).toList();

        Optional<Items> itemsFind = itemsRepository.findByName(String.valueOf(itemsRequest.getItemsName()));
        if (itemsFind.isPresent()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Items already exists", itemsRequest.getItemsName());
        }

        Optional<Category> categoryFind = categoryRepository.findByUUIDString(itemsRequest.getCategoryId());
        if (categoryFind.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Category not exists", itemsRequest.getCategoryId());
        }

        Items items = new Items(
                itemsRequest.getItemsName(),
                List.of(categoryFind.get()),
                List.of(categoryFind.get().getPages()),
                roles.contains("ROLE_ADMIN")
        );
        if (itemsRequest.getDescription() != null) {items.setDescription(itemsRequest.getDescription());}
        Items itemCreated = itemsRepository.save(items);
        return responseEntityAppResponse.getAppResponse(HttpStatus.OK, String.format("Items created %s", itemsRequest.getItemsName()), itemCreated.getId().toString());
    }

}
