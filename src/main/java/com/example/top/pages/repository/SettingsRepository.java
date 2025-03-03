package com.example.top.pages.repository;

import com.example.top.pages.models.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface SettingsRepository extends JpaRepository<Settings, String> {
    @Query(value = "select s.value from settings s where s.\"name\" = :name", nativeQuery = true)
    String findValueByName(String name);

}

