package com.example.top.pages.repository;

import com.example.top.pages.models.Pages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface PagesRepository extends JpaRepository<Pages, UUID> {

    @Query(value = "select * from pages c where id = :uuid", nativeQuery = true)
    Optional<Pages> findByUUID(UUID uuid);
}
