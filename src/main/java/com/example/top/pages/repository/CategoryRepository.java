package com.example.top.pages.repository;

import com.example.top.pages.models.Categories;
import com.example.top.pages.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Categories, String> {
}
