package com.example.top.pages.repository;

import com.example.top.pages.models.Items;
import com.example.top.pages.models.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RateRepository extends JpaRepository<Rate, String> {

    @Query(value = "select * from rate r left join users u on r.user_id = u.id where u.email = :email", nativeQuery = true)
    List<Rate> getRateFromEmailUser(String email);

    @Query(value = "select * from rate r where user_id = :userUUID", nativeQuery = true)
    Optional<Rate> getRateByUser(String userUUID);

    @Query(value = "select * from rate r where user_id = :userUUID", nativeQuery = true)
    List<Rate> getListRatesByIdUser(String userUUID);

    @Query(value = "select * from rate r where user_id = :userUUID and category_id = :categoryUUID", nativeQuery = true)
    Optional<Rate> getRateByIDAndCategory(String userUUID, String categoryUUID);

    @Query(value = "select * from rate r where id = :rateUUID", nativeQuery = true)
    Optional<Rate> getRateByUUID(String rateUUID);

    @Query(value="select r.id, r.\"comment\", r.user_id, r.ispositive, r.isapproved, r.item, i.\"name\", r.category_id from rate r left join item i on r.item = i.id where r.user_id = :userUUID and i.id = :itemUUID and r.category_id = :categoryId", nativeQuery = true)
    Optional<Rate> getItemAndRateByUserAndItemAndCategory(String userUUID, String itemUUID, String categoryId);
}
