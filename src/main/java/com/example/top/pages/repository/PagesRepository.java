package com.example.top.pages.repository;

import com.example.top.pages.models.Pages;
import com.example.top.pages.models.Rate;
import com.example.top.pages.payload.response.SearchPagesNames;
import com.example.top.pages.repository.models.Pages.PagesCategory;
import com.example.top.pages.repository.models.Pages.PagesItems;
import com.example.top.pages.repository.models.Pages.PagesItemsExistedRates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PagesRepository extends JpaRepository<Pages, UUID> {

    @Query(value = "select * from pages p where p.id = :id", nativeQuery = true)
    Pages getStrictPagesByID(String id);

    @Query(value = "select pi2.items_id as item_id, count(r.id) as count " +
            "from pages_items pi2 " +
            "left join rate r on pi2.items_id = r.item " +
            "where pi2.pages_id = :pagesId and r.category_id = :categoryId " +
            "group by pi2.items_id\n", nativeQuery = true)
    List<PagesItemsExistedRates> getExistedRatesFromItemsByCategoryAndPages(String pagesId, String categoryId);

    @Query(value = "select * from pages c where id = :uuid", nativeQuery = true)
    Optional<Pages> findByUUIDString(String uuid);

    @Query(value = "select * from pages p where title = :name", nativeQuery = true)
    Optional<Pages> getPagesByName(String name);

    @Query(value = "select p.id from pages p", nativeQuery = true)
    List<String> getAllPagesIds();

    @Query(value = "SELECT p.title, p.id from pages p WHERE p.title LIKE %:word%", nativeQuery = true)
    List<SearchPagesNames> searchTitlesByWord(String word);

    @Query(value = "select p.id as pages_id, p.title, p.description_pages," +
            " p.isapproved as isapproved, p.category as category_id, c.\"name\" as name_category, c.description from pages p " +
            "left join category c on p.category = c.id " +
            "where p.title = :title", nativeQuery = true)
    Optional<PagesCategory> getAllPagesWithCategoryByTitle(String title);

    @Query(value = "select p.id as pages_id, p.title, p.description_pages," +
            " p.status, p.category as category_id, c.\"name\" as name_category, c.description " +
            "from pages p left join category c on p.category = c.id " +
            "where p.id = :uuid", nativeQuery = true)
    Optional<PagesCategory> getAllPagesWithCategoryByUUID(String uuid);
}
