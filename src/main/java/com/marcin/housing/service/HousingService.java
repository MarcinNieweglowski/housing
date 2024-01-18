package com.marcin.housing.service;

import com.marcin.housing.model.Housing;
import com.marcin.housing.model.HousingSize;
import com.marcin.housing.model.HousingType;
import com.marcin.housing.model.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.marcin.housing.service.HousingSpecification.*;
import static org.springframework.data.jpa.domain.Specification.allOf;

@Component
@Slf4j
public class HousingService {

    @Autowired
    private HousingRepository housingRepository;

    public void saveHousingData(List<Housing> housings) {
        log.info("About to persist data of {} housings", housings.size());
        housingRepository.saveAll(housings);
    }

    public BigDecimal getAverageValueForGivenFilters(Region region, HousingSize size, Integer rooms,
                                                     List<HousingType> types, LocalDate dateSince, LocalDate dateUntil) {
        List<Housing> housings = fetchHousingData(region, size, rooms, types, dateSince, dateUntil);

        if (housings.isEmpty()) {
            log.info("No housings found for given values");
            return BigDecimal.ZERO;
        }

        log.info("Found {} housings that fulfilled the given values", housings.size());

        BigDecimal averageValue = housings.stream()
                .map(Housing::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(housings.size()));

        return averageValue;
    }

    public List<Housing> fetchHousingData(Region region, HousingSize size, Integer rooms,
                                          List<HousingType> types, LocalDate dateSince, LocalDate dateUntil) {
        List<Specification<Housing>> queryFilters = new ArrayList<>();

        if (region != null) {
            queryFilters.add(hasRegion(region));
        }
        if (size != null) {
            queryFilters.add(hasSizeBetween(size));
        }
        if (rooms != null && rooms > 0) {
            queryFilters.add(hasRoomsEqualTo(rooms));
        }
        if (dateSince != null) {
            queryFilters.add(hasDateAfter(dateSince));
        }
        if (dateUntil != null) {
            queryFilters.add(hasDateBefore(dateUntil));
        }
        if (types != null && !types.isEmpty()) {
            queryFilters.add(hasTypesIn(types));
        }
        return housingRepository.findAll(allOf(queryFilters));
    }
}
