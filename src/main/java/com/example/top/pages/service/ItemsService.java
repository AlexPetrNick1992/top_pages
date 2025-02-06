package com.example.top.pages.service;

import com.example.top.pages.models.Category;
import com.example.top.pages.models.Items;
import com.example.top.pages.repository.ItemsRepository;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

@Service
public class ItemsService {

    private final ItemsRepository itemsRepository;
    private final CategoryService categoryService;

    public ItemsService(ItemsRepository itemsRepository, CategoryService categoryService) {
        this.itemsRepository = itemsRepository;
        this.categoryService = categoryService;
    }

    public List<Items> getItemsList() {
        return itemsRepository.findAll();
    }

    public Items createItems(Items items) {
        Optional<Items> itemsFind = itemsRepository.findByStringUUID(String.valueOf(items.getId()));
        if (itemsFind.isPresent()) {
            throw new IllegalStateException("Items has exists");
        }
        Optional<Category> categoryFind = categoryService.findCategoryByUUID(items.getCategory().getId());
        if (categoryFind.isEmpty()) {
            throw new IllegalStateException("Category hasn't exists");
        } else {
            items.setCategory(categoryFind.get());
        }
        return itemsRepository.save(items);
    }
}
