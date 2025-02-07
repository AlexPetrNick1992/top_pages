package com.example.top.pages.controller;

import com.example.top.pages.models.Rate;
import com.example.top.pages.payload.request.RateAction;
import com.example.top.pages.payload.request.RateUpdate;
import com.example.top.pages.service.RateService;
import jakarta.validation.Valid;
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

    @GetMapping
    public List<Rate> getListRatesForUser() {
        return rateService.getListRatesForUser();
    }

    @GetMapping("/approve")
    public ResponseEntity<?> approve(
            @RequestParam(required = true, name = "rate_id") String rateId,
            @RequestParam(required = true, name = "category_id") String categoryId
    ) {
        return rateService.approve(rateId, categoryId);
    }

    @GetMapping("/disprove")
    public ResponseEntity<?> disprove(
            @RequestParam(required = true, name = "rate_id") String rateId,
            @RequestParam(required = true, name = "category_id") String categoryId
    ) {
        return rateService.disprove(rateId, categoryId);
    }

    @PostMapping("/action")
    public ResponseEntity<?> rateToItem(@Valid @RequestBody RateAction rateAction) {
        return rateService.rateToItem(rateAction);
    }

    @PutMapping()
    public ResponseEntity<?> updateRate(@RequestParam(required = true, name = "rate_id") String rateId
            , @Valid @RequestBody RateUpdate comment) {
        return rateService.updateRate(rateId, comment);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteRate(
            @RequestParam(required = true, name = "rate_id") String rateId,
            @RequestParam(required = true, name = "category_id") String categoryId
    ) {
        return rateService.deleteRate(rateId, categoryId);
    }

}
