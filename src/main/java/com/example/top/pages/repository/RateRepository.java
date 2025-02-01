package com.example.top.pages.repository;

import com.example.top.pages.models.Items;
import com.example.top.pages.models.Rate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRepository extends JpaRepository<Rate, String> {
}
