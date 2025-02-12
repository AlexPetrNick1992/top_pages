package com.example.top.pages.repository;

import com.example.top.pages.models.Items;
import com.example.top.pages.repository.models.Items.ItemsCategory;
import com.example.top.pages.repository.models.Pages.ReturnCountRates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemsRepository extends JpaRepository<Items, String> {

    @Query(value = "select * from item c where id = :uuid", nativeQuery = true)
    Optional<Items> findByStringUUID(String uuid);

    @Query(value = "select * from item c where id = :uuid", nativeQuery = true)
    Items findByStringUUIDStrict(String uuid);

    @Query(value = "select * from item c where id = :uuid", nativeQuery = true)
    Optional<Items> findByUUID(UUID uuid);

    @Query(value = "select * from item i where name = :name", nativeQuery = true)
    Optional<Items> findByName(String name);

    @Query(value = "select i.id, i.\"name\", i.description, count(r.id) " +
            "from item i join rate r on r.item = i.id " +
            "where r.item in :listItems and r.ispositive = :isPositive group by i.id", nativeQuery = true)
    List<ReturnCountRates> getCountRatesByItems(List<String> listItems, Boolean isPositive);

    @Query(value = "select i.id, i.\"name\", i.description, count(r.id) " +
            "from item i join rate r on r.item = i.id " +
            "where r.item in :listItems group by i.id", nativeQuery = true)
    List<ReturnCountRates> getCountRatesByItemsAll(List<String> listItems);

    @Query(value = "select i.id as item_id, i.name as item_name, i.description, i.isapproved " +
            "from item i " +
            "join item_category ic on ic.item_id = i.id " +
            "join category c on c.id = ic.category_id " +
            "where c.id = :categoryId", nativeQuery = true)
    List<ItemsCategory> getItemsByCategory(String categoryId);

}
