package com.example.top.pages.payload.response;

import com.example.top.pages.models.Rate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
public class UserRatesResponse {
    @JsonIgnore
    List<Rate> rateList;
    List<Map<String, Object>> rates;

    public UserRatesResponse(List<Rate> rateList) {
        this.rates = rateList.stream().map(rate -> {
            Map<String, Object> mapRates = new HashMap<String, Object>();
            Map<String, Object> ratesCategory = new HashMap<String, Object>();
            mapRates.put("comment", rate.getComment());
            mapRates.put("is_positive", rate.isPositive());
            mapRates.put("is_approved", rate.isApproved());
            ratesCategory.put("id", rate.getCategory().getId());
            ratesCategory.put("name", rate.getCategory().getName());
            mapRates.put("category", ratesCategory);
            return mapRates;
        }).toList();
    }
}
