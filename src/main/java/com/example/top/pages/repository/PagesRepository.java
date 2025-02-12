package com.example.top.pages.repository;

import com.example.top.pages.models.Pages;
import com.example.top.pages.models.Rate;
import com.example.top.pages.repository.models.Pages.PagesCategory;
import com.example.top.pages.repository.models.Pages.PagesItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PagesRepository extends JpaRepository<Pages, UUID> {

    @Query(value = "select * from pages c where id = :uuid", nativeQuery = true)
    Optional<Pages> findByUUID(UUID uuid);
    @Query(value = "select p.id, p.title, p.description_pages, p.category, p.isapproved from pages_items pi2 left join pages p on p.id = pi2.pages_id", nativeQuery = true)
    List<Pages> getListPagesWithItems();
    @Query(value = "select * from pages c where id = :uuid", nativeQuery = true)
    Optional<Pages> findByUUIDString(String uuid);
    @Query(value = "select * from pages p where title = :name", nativeQuery = true)
    Optional<Pages> getPagesByName(String name);

    @Query(value = "select p.id from pages p", nativeQuery = true)
    List<String> getAllPagesIds();

    @Query(value = "select pi2.pages_id, pi2.items_id from pages_items pi2 where pi2.pages_id in :listPagesId", nativeQuery = true)
    List<PagesItems> getItemsPages(List<String> listPagesId);

    @Query(value = "select r.* from pages p left join rate r on r.category_id = p.category where p.id = :pagesUUID", nativeQuery = true)
    List<Rate> getListRatesByPages(String pagesUUID);

    @Query(value = "select p.id as pages_id, p.title, p.description_pages , p.isapproved as isapproved, p.category as category_id, c.\"name\" as name_category, c.description from pages p left join category c on p.category = c.id", nativeQuery = true)
    List<PagesCategory> getAllPagesWithCategory();

    @Query(value = "select p.id as pages_id, p.title, p.description_pages," +
            " p.isapproved as isapproved, p.category as category_id, c.\"name\" as name_category, c.description from pages p " +
            "left join category c on p.category = c.id " +
            "where p.title = :title", nativeQuery = true)
    Optional<PagesCategory> getAllPagesWithCategoryByTitle(String title);
    @Query(value = "select p.id as pages_id, p.title, p.description_pages," +
            " p.isapproved as isapproved, p.category as category_id, c.\"name\" as name_category, c.description " +
            "from pages p left join category c on p.category = c.id " +
            "where p.id = :uuid", nativeQuery = true)
    Optional<PagesCategory> getAllPagesWithCategoryByUUID(String uuid);
}
