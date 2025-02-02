package com.example.top.pages.repository;

import com.example.top.pages.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query(value = "select * from category c where id = :uuid", nativeQuery = true)
    Optional<Category> findByUUID(String uuid);
}
