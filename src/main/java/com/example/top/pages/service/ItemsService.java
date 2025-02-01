package com.example.top.pages.service;

import com.example.top.pages.models.Categories;
import com.example.top.pages.models.Items;
import com.example.top.pages.repository.ItemsRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemsService {

    private final ItemsRepository itemsRepository;

    public ItemsService(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    public List<Items> getItemsList() {
        List<Items> a = itemsRepository.findAll();
        System.out.println(a);
        return a;
    }
}
