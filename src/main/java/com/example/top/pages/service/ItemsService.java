package com.example.top.pages.service;

import com.example.top.pages.models.Category;
import com.example.top.pages.models.Items;
import com.example.top.pages.models.User;
import com.example.top.pages.payload.request.ItemsRequest;
import com.example.top.pages.payload.response.ItemsListResponse;
import com.example.top.pages.payload.response.ResponseEntityAppResponse;
import com.example.top.pages.repository.CategoryRepository;
import com.example.top.pages.repository.ItemsRepository;
import com.example.top.pages.repository.PagesRepository;
import com.example.top.pages.repository.UserRepository;
import com.example.top.pages.repository.models.Items.ItemsCategory;
import com.example.top.pages.utils.DateWorker;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.*;

@Data
@RequiredArgsConstructor
@Service
public class ItemsService {
    private final ItemsRepository itemsRepository;
    private final CategoryRepository categoryRepository;
    private final ResponseEntityAppResponse responseEntityAppResponse;
    private final PagesRepository pagesRepository;
    private final UserRepository userRepository;
    private final DateWorker dateWorker;
    private final ActionHistoryService actionHistoryService;

    public ResponseEntity<?> getItemsListForTest() {
        return ResponseEntity.ok(itemsRepository.findAll());
    }

    // TODO: Оптимизировать
    public ResponseEntity<?> getItemsList(String categoryId) {
        List<ItemsCategory> itemsList = itemsRepository.getItemsByCategory(categoryId);
        return ResponseEntity.ok(new ItemsListResponse(itemsList));
    }

    public ResponseEntity<?> createItems(ItemsRequest itemsRequest) {
        Authentication contextUser = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = contextUser.getAuthorities().stream().map(Object::toString).toList();
        User user = userRepository.findCheckedUserByEmail(contextUser.getPrincipal().toString());

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
                roles.contains("ROLE_ADMIN"),
                user, dateWorker.getCurrentTimeISO()
        );
        if (itemsRequest.getDescription() != null) {items.setDescription(itemsRequest.getDescription());}
        Items itemCreated = itemsRepository.save(items);
        actionHistoryService.createHistoryByType(
                ActionHistoryService.TypeHistory.ITEM_CREATE, user, itemsRequest.getItemsName()
        );
        return responseEntityAppResponse.getAppResponse(HttpStatus.OK, String.format("Items created %s", itemsRequest.getItemsName()), itemCreated.getId().toString());
    }

    public ResponseEntity<?> getUsersRatedItems(String categoryId) {
        Authentication contextUser = SecurityContextHolder.getContext().getAuthentication();
        User userItem = userRepository.findCheckedUserByEmail(contextUser.getPrincipal().toString());
        return ResponseEntity.ok(new HashMap<>(){{
            put("items_id", itemsRepository.findByName(userItem.getId().toString(), categoryId));
        }});
    }

    public ResponseEntity<?> searchItem(String text) {
        return ResponseEntity.ok(itemsRepository.searchItems(text));
    }
}
