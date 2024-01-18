package com.marcin.housing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcin.housing.model.HousingSize;
import com.marcin.housing.model.HousingType;
import com.marcin.housing.model.Region;
import com.marcin.housing.service.HousingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/real-estates-stats")
@Slf4j
public class RealEstatesStatsController {

    @Autowired
    private HousingService housingService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/{regionID}")
    public RealEstatesStatsResponse getAveragePrice(@PathVariable("regionID") Region region,
                                                    @RequestParam(value = "size", required = false) HousingSize size,
                                                    @RequestParam(value = "rooms", required = false) Integer rooms,
                                                    @RequestParam(value = "types", required = false) List<HousingType> types,
                                                    @RequestParam(value = "dateSince", required = false) @DateTimeFormat(pattern = "yyyyMMdd") LocalDate dateSince,
                                                    @RequestParam(value = "dateUntil", required = false) @DateTimeFormat(pattern = "yyyyMMdd") LocalDate dateUntil) {
        log.info("About to calculate average value for region: '{}'", region);

        BigDecimal averageValue = housingService.getAverageValueForGivenFilters(region, size, rooms, types, dateSince, dateUntil);

        return new RealEstatesStatsResponse(String.valueOf(averageValue));
    }

    private record RealEstatesStatsResponse(String avgValue) {
    }
}
