package com.example.top.pages.controller;

import com.example.top.pages.models.Items;
import com.example.top.pages.models.Rate;
import com.example.top.pages.service.RateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(path="api/v1/rate")
public class RateController {

    private final RateService rateService;

    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    @GetMapping
    public List<Rate> getRate() {
        return rateService.getRateList();
    }

    @GetMapping(path = "/as")
    public List<Rate> getItems() {
        return rateService.getRateList();
    }
}
