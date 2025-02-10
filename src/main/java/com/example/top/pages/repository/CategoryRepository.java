package com.example.top.pages.repository;

import com.example.top.pages.models.Category;
import com.example.top.pages.models.Pages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query(value = "select * from category c where id = :uuid", nativeQuery = true)
    Optional<Category> findByUUID(UUID uuid);
    @Query(value = "select * from category c where id = :uuid", nativeQuery = true)
    Optional<Category> findByUUIDString(String uuid);

    @Query(value = "select * from category c where id = :uuid", nativeQuery = true)
    Category findByUUIDStringStrick(String uuid);

    @Query(value = "select p.* from category c left join pages p ON p.category = c.id where c.id = :uuid", nativeQuery = true)
    Optional<Pages> findPagesByCategoryUUID(String uuid);

    @Query(value = "select * from category c where name = :nameCategory", nativeQuery = true)
    Optional<Category> findCategoryByName(String nameCategory);

    @Query(value = "select c.* from category c left join pages p on p.category = c.id where p.id = :pagesUUID", nativeQuery = true)
    Category getCategoryByIdPages(String pagesUUID);
}
