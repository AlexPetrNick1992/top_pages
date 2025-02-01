package com.example.top.pages.repository;

import com.example.top.pages.models.Items;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemsRepository extends JpaRepository<Items, String> {
}
