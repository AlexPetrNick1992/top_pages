package com.example.top.pages.service;

import com.example.top.pages.models.Category;
import com.example.top.pages.models.Items;
import com.example.top.pages.models.Rate;
import com.example.top.pages.models.User;
import com.example.top.pages.payload.request.ItemsRequest;
import com.example.top.pages.payload.response.ItemsListResponse;
import com.example.top.pages.payload.response.ResponseEntityAppResponse;
import com.example.top.pages.repository.CategoryRepository;
import com.example.top.pages.repository.ItemsRepository;
import com.example.top.pages.repository.PagesRepository;
import com.example.top.pages.repository.UserRepository;
import com.example.top.pages.repository.models.Items.ItemsCategory;
import com.example.top.pages.service.enumerate.StatusEntity;
import com.example.top.pages.utils.DateWorker;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // TODO: Оптимизировать
    public ResponseEntity<?> getItemsList(String categoryId) {
        List<ItemsCategory> itemsList = itemsRepository.getItemsByCategory(categoryId);
        return ResponseEntity.ok(new ItemsListResponse(itemsList));
    }

    @Transactional
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

        StatusEntity newStatus;
        if (roles.contains("ROLE_ADMIN") || roles.contains("ROLE_APPROVED")) newStatus = StatusEntity.APPROVED;
        else newStatus = StatusEntity.WAIT_APPROVE;

        Items items = new Items(
                itemsRequest.getItemsName(),
                List.of(categoryFind.get()),
                List.of(categoryFind.get().getPages()),
                newStatus,
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

    @Transactional
    public ResponseEntity<?> approveItem(String itemId) {
        Authentication contextUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findCheckedUserByEmail(contextUser.getPrincipal().toString());

        /* Проверка категории */
        Optional<Items> itemsCheck = itemsRepository.findByStringUUID(itemId);
        if (itemsCheck.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Item not exists", itemId);
        }
        Items items = itemsCheck.get();
        if (items.getStatus() == StatusEntity.APPROVED) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "This item already is approved", null);
        }
        items.setStatus(StatusEntity.APPROVED);
        itemsRepository.save(items);
        actionHistoryService.createHistoryByTypeWithAdmin(
                ActionHistoryService.TypeHistoryAdmin.APPROVED_ITEM,
                String.format("%s(%s)", user.getName(), user.getId().toString()),
                itemId,
                items.getCreatedBy()
        );
        return responseEntityAppResponse.getAppResponse(HttpStatus.OK, "Items successfully approved", items.getId().toString());
    }

    @Transactional
    public ResponseEntity<?> disapproveItem(String itemId) {
        return ResponseEntity.ok("SA");
    }

    @Transactional
    public ResponseEntity<?> deleteItem(String itemId) {
        Authentication contextUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findCheckedUserByEmail(contextUser.getPrincipal().toString());

        Optional<Items> itemsCheck = itemsRepository.findByStringUUID(itemId);
        if (itemsCheck.isEmpty()) {
            return responseEntityAppResponse.getAppResponse(HttpStatus.BAD_REQUEST, "Item not exists", itemId);
        }
        Items items = itemsCheck.get();
        itemsRepository.delete(items);
        actionHistoryService.createHistoryByTypeWithAdmin(
                ActionHistoryService.TypeHistoryAdmin.DELETED_ITEM,
                String.format("%s(%s)", user.getName(), user.getId().toString()),
                itemId,
                items.getCreatedBy()
        );
        return responseEntityAppResponse.getAppResponse(HttpStatus.OK, "Item successfully delete", itemId);
    }
}
