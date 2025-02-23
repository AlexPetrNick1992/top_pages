package com.example.top.pages.repository;

import com.example.top.pages.models.Items;
import com.example.top.pages.models.Rate;
import com.example.top.pages.payload.response.RateRequest;
import com.example.top.pages.payload.response.UserInfoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RateRepository extends JpaRepository<Rate, String> {
    @Query(value = "select * from rate r where user_id = :userUUID and r.status not in (1,3)", nativeQuery = true)
    List<Rate> getListRatesByIdUser(String userUUID);

    @Query(value = "select r.id as rate_id, r.\"comment\" as rate_comment, r.ispositive, r.status, r.category_id, r.created_at from rate r where r.user_id = :userId and r.status = 1", nativeQuery = true)
    List<RateRequest> getRequestRate(String userId);

    @Query(value = "select * from rate r where user_id = :userUUID and category_id = :categoryUUID", nativeQuery = true)
    Optional<Rate> getRateByIDAndCategory(String userUUID, String categoryUUID);

    @Query(value = "select u.id, u.\"name\", u.email, u.birthday, u.created_at, " +
            "(select count(*) from rate r where r.user_id = u.id and r.status not in (1,3)) " +
            "from users u " +
            "where u.id = :userUUID", nativeQuery = true)
    Optional<UserInfoResponse> getUserInfoWithARates(String userUUID);

    @Query(value = "select * from rate r where id = :rateUUID", nativeQuery = true)
    Optional<Rate> getRateByUUID(String rateUUID);

    @Query(value = "select * from rate r where r.item = :itemId and r.category_id = :categoryId and r.status not in (1,3) order by created_at desc limit :limit", nativeQuery = true)
    List<Rate> getRateByItem(String itemId, String categoryId, Integer limit);

    @Query(value = "select * from rate r where r.item = :itemId and r.category_id = :categoryId and r.status not in (1,3) order by created_at desc", nativeQuery = true)
    List<Rate> getRateByItemWithoutLimit(String itemId, String categoryId);

    @Query(value="select r.created_at, r.updated_at, r.id, r.\"comment\", r.user_id, r.ispositive, r.status, r.item, i.\"name\", r.category_id from rate r left join item i on r.item = i.id where r.user_id = :userUUID and i.id = :itemUUID and r.category_id = :categoryId", nativeQuery = true)
    Optional<Rate> getItemAndRateByUserAndItemAndCategory(String userUUID, String itemUUID, String categoryId);
}
