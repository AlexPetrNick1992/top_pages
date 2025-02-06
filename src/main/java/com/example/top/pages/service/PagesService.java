package com.example.top.pages.service;

import com.example.top.pages.models.Category;
import com.example.top.pages.models.Pages;
import com.example.top.pages.repository.CategoryRepository;
import com.example.top.pages.repository.PagesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PagesService {

    private final PagesRepository pagesRepository;

    public List<Pages> getPagesList() {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(pagesRepository.findAll());
        return pagesRepository.findAll();
    }

    public Pages getPageByUUID (UUID id) {
        Optional<Pages> category = pagesRepository.findByUUID(id);
        if (category.isEmpty()) {
            throw new IllegalStateException("Не найдена страница");
        } else {
            return category.get();
        }
    }

}
