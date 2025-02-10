package com.example.top.pages.controller;

import com.example.top.pages.models.Items;
import com.example.top.pages.payload.request.ItemsRequest;
import com.example.top.pages.service.ItemsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(path="api/items")
public class ItemsController {

    private final ItemsService itemsService;

    public ItemsController(ItemsService itemsService) {
        this.itemsService = itemsService;
    }

    @GetMapping
    public ResponseEntity<?> getItems() {
        return itemsService.getItemsList();
    }

    @PostMapping
    public ResponseEntity<?> createItems(@Valid @RequestBody ItemsRequest itemsRequest) {
        return itemsService.createItems(itemsRequest);
    }

}
