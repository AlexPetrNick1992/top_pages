package com.example.top.pages.repository;

import com.example.top.pages.models.ActionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActionHistoryRepository extends JpaRepository<ActionHistory, String> {

    @Query(value = "select ah.history from action_history ah where ah.user_id = :userId", nativeQuery = true)
    List<String> findHistoryByUserId(String userId);
}
