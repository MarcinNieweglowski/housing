package com.marcin.housing.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.marcin.housing.model.HousingType;
import com.marcin.housing.model.Region;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES;

public interface HousingClient {

    @GetExchange("/{regionID}")
    HousingDataResponse getHousingDataResponse(@PathVariable("regionID") Region regionID,
                                               @RequestParam(value = "page", required = false) @Positive int page);


    record HousingDataResponse(int totalPages, List<HousingResponse> data) {
    }


    record HousingResponse(UUID id, @JsonFormat(with = ACCEPT_CASE_INSENSITIVE_PROPERTIES) HousingType type,
                           String price, String description, String area, int rooms) {
    }

}
