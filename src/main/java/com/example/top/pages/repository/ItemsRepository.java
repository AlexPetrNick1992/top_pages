package com.example.top.pages.repository;

import com.example.top.pages.models.Items;
import com.example.top.pages.payload.response.ItemsSearch;
import com.example.top.pages.repository.models.Items.ItemsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItemsRepository extends JpaRepository<Items, String> {

    @Query(value = "select * from item c where id = :uuid", nativeQuery = true)
    Optional<Items> findByStringUUID(String uuid);

    @Query(value = "select * from item i where name = :name", nativeQuery = true)
    Optional<Items> findByName(String name);

    @Query(value = "select r.item from rate r where r.user_id = :userId and r.category_id = :categoryId", nativeQuery = true)
    List<String> findByName(String userId, String categoryId);

    @Query(value = "select i.id as item_id, i.name as item_name, i.description, i.status " +
            "from item i " +
            "join item_category ic on ic.item_id = i.id " +
            "join category c on c.id = ic.category_id " +
            "where c.id = :categoryId", nativeQuery = true)
    List<ItemsCategory> getItemsByCategory(String categoryId);

    @Query(value = "select i.id, i.\"name\", i.description  from item i where lower(i.\"name\") like %:word%", nativeQuery = true)
    List<ItemsSearch> searchItems(String word);
}

