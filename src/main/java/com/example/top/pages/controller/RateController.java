package com.example.top.pages.controller;

import com.example.top.pages.models.Rate;
import com.example.top.pages.payload.request.RateAction;
import com.example.top.pages.service.RateService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RequiredArgsConstructor
@RestController
@RequestMapping(path="api/v1/rate")
public class RateController {
    private final RateService rateService;

    @PostMapping("/action")
    public ResponseEntity<?> rateToItem(@RequestBody RateAction rateAction) {
        System.out.println(rateAction);
        return rateService.rateToItem(rateAction);
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
