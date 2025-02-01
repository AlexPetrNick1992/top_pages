package com.example.top.pages.service;

import com.example.top.pages.models.Rate;
import com.example.top.pages.repository.RateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RateService {
    private final RateRepository rateRepository;
    public RateService(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }
    public List<Rate> getRateList() {return rateRepository.findAll();}
}
