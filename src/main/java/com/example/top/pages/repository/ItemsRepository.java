package com.example.top.pages.repository;

import com.example.top.pages.models.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ItemsRepository extends JpaRepository<Items, String> {

    @Query(value = "select * from items c where id = :uuid", nativeQuery = true)
    Optional<Items> findByUUID(UUID uuid);

}
