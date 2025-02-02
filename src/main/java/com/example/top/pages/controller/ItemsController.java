package com.example.top.pages.controller;

import com.example.top.pages.models.Items;
import com.example.top.pages.service.ItemsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path="api/items")
public class ItemsController {

    private final ItemsService itemsService;

    public ItemsController(ItemsService itemsService) {
        this.itemsService = itemsService;
    }

    @GetMapping
    public List<Items> getItems() {
        return itemsService.getItemsList();
    }

    @PostMapping
    public Items createItems(@RequestBody Items items) {
        return itemsService.createItems(items);
    }
}
