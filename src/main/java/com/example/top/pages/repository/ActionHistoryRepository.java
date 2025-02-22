package com.example.top.pages.repository;

import com.example.top.pages.models.ActionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionHistoryRepository extends JpaRepository<ActionHistory, String> {

}
