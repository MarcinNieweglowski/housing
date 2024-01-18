package com.marcin.housing.utils;

import com.marcin.housing.model.Housing;
import com.marcin.housing.model.HousingType;
import com.marcin.housing.model.Region;
import com.marcin.housing.service.HousingClient.HousingResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class HousingGenerator {
    public static Housing generateHousing(HousingType type, BigDecimal price, double area, int rooms, Region region, LocalDate date) {
        Housing housing = new Housing();

        housing.setId(UUID.randomUUID());
        housing.setType(type);
        housing.setPrice(price);
        housing.setArea(area);
        housing.setRooms(rooms);
        housing.setRegion(region);
        housing.setDateAdded(date);

        return housing;
    }

    public static Housing createHousingFromResponseData(HousingResponse housingResponse, Region region) {
        Housing mappedHousingData = new Housing();

        mappedHousingData.setDateAdded(LocalDate.now());
        mappedHousingData.setRegion(region);
        mappedHousingData.setId(housingResponse.id());
        mappedHousingData.setRooms(housingResponse.rooms());
        mappedHousingData.setPrice(new BigDecimal(housingResponse.price()));
        mappedHousingData.setType(housingResponse.type());
        mappedHousingData.setArea(Double.parseDouble(housingResponse.area()));

        return mappedHousingData;
    }
}
