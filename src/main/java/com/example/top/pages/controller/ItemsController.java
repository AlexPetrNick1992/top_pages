package com.example.top.pages.controller;

import com.example.top.pages.payload.request.ItemsRequest;
import com.example.top.pages.service.ItemsService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@Data
@RequiredArgsConstructor
@RestController
@RequestMapping(path="api/items")
public class ItemsController {
    private final ItemsService itemsService;

    @GetMapping("/approve")
    public ResponseEntity<?> approveItem(
            @RequestParam(required = true, name = "item_id") String itemId
    ) {
        return itemsService.approveItem(itemId);
    }
    @GetMapping("/disprove")
    public ResponseEntity<?> disapproveItem(
            @RequestParam(required = true, name = "item_id") String itemId
    ) {
        return itemsService.disapproveItem(itemId);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteItem(
            @RequestParam(required = true, name = "item_id") String item_id
    ) {
        return itemsService.deleteItem(item_id);
    }

    @PostMapping
    public ResponseEntity<?> createItems(@Valid @RequestBody ItemsRequest itemsRequest) {
        return itemsService.createItems(itemsRequest);
    }

    @GetMapping
    public ResponseEntity<?> getItems(
            @RequestParam(required = true, name = "category_id") String categoryId
    ) {
        return itemsService.getItemsList(categoryId);
    }

    @GetMapping("/users_rated_items")
    public ResponseEntity<?> getUsersRatedItems(
            @RequestParam(required = true, name = "category_id") String categoryId
    ) {
        return itemsService.getUsersRatedItems(categoryId);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchItem(
            @RequestParam(required = true, name = "text") String text
    ) {
        return itemsService.searchItem(text);
    }

}
