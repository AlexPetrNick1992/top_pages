package com.example.top.pages.repository;

import com.example.top.pages.models.Pages;
import com.example.top.pages.models.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PagesRepository extends JpaRepository<Pages, UUID> {

    @Query(value = "select * from pages c where id = :uuid", nativeQuery = true)
    Optional<Pages> findByUUID(UUID uuid);
    @Query(value = "select * from pages c where id = :uuid", nativeQuery = true)
    Optional<Pages> findByUUIDString(String uuid);
    @Query(value = "select * from pages p where name = :name", nativeQuery = true)
    Optional<Pages> getPagesByName(String name);

    @Query(value = "select r.* from pages p left join rate r on r.category_id = p.category where p.id = :pagesUUID", nativeQuery = true)
    List<Rate> getListRatesByPages(String pagesUUID);
}
