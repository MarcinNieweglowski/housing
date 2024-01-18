package com.marcin.housing.controller;

import com.marcin.housing.model.Housing;
import com.marcin.housing.model.HousingSize;
import com.marcin.housing.model.HousingType;
import com.marcin.housing.model.Region;
import com.marcin.housing.utils.BaseSpringBootTestSetup;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.marcin.housing.model.Region.DLN_WROC_C;
import static com.marcin.housing.utils.HousingGenerator.generateHousing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RealEstatesStatsControllerTest extends BaseSpringBootTestSetup {

    private static final String BASE_CONTROLLER_MAPPING = "api/real-estates-stats";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("getAveragePriceParams")
    void getAveragePrice_shouldCalculateValue_fromFilteredData(List<Housing> housings,
                                                               ControllerQueryParams queryParams,
                                                               BigDecimal expectedAverageValue) {
        housingRepository.saveAll(housings);

        MockHttpServletRequestBuilder requestBuilder = get("/%s/%s".formatted(BASE_CONTROLLER_MAPPING, queryParams.region()));
        mockMvc.perform(
                addParams(requestBuilder, queryParams).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.avgValue").value(expectedAverageValue.toString()));
    }

    private MockHttpServletRequestBuilder addParams(MockHttpServletRequestBuilder requestBuilder, ControllerQueryParams queryParams) {
        if (queryParams.size() != null) {
            requestBuilder.param("size", queryParams.size().toString());
        }
        if (queryParams.rooms() != null) {
            requestBuilder.param("rooms", queryParams.rooms().toString());
        }
        if (queryParams.types() != null && !queryParams.types().isEmpty()) {
            String types = queryParams.types().stream().map(type -> String.valueOf(type)).collect(Collectors.joining(","));
            requestBuilder.param("types", types);
        }
        if (queryParams.dateSince() != null) {
            requestBuilder.param("dateSince", queryParams.dateSince().format(DATE_FORMATTER));
        }
        if (queryParams.dateUntil() != null) {
            requestBuilder.param("dateUntil", queryParams.dateUntil().format(DATE_FORMATTER));
        }
        return requestBuilder;
    }

    private static Stream<Arguments> getAveragePriceParams() {
        // 1
        BigDecimal price1 = BigDecimal.valueOf(567899.99);
        BigDecimal price2 = BigDecimal.valueOf(654545.99);
        BigDecimal price3 = BigDecimal.valueOf(666666.99);
        BigDecimal price4 = BigDecimal.valueOf(555555.65);

        LocalDate date = LocalDate.now();
        Housing housing = generateHousing(HousingType.FLAT, price1, 75, 2,
                DLN_WROC_C, date);
        Housing housing2 = generateHousing(HousingType.TERRACED_HOUSE, price2, 66, 2,
                DLN_WROC_C, date);
        Housing housing3 = generateHousing(HousingType.FLAT, price3, 99, 1,
                DLN_WROC_C, date);
        Housing housing4 = generateHousing(HousingType.FLAT, price4, 71, 2,
                Region.LUBL, date);

        ControllerQueryParams queryParams = new ControllerQueryParams(DLN_WROC_C, HousingSize.M, 2,
                List.of(HousingType.FLAT, HousingType.TERRACED_HOUSE), date.minusDays(3), date);

        // 2
        ControllerQueryParams queryParams2 = new ControllerQueryParams(DLN_WROC_C, HousingSize.L, 3,
                null, date.minusDays(3), date);
        Housing housing5 = generateHousing(HousingType.FLAT, price4, 85, 3,
                DLN_WROC_C, date);

        // 3
        ControllerQueryParams queryParams3 = new ControllerQueryParams(DLN_WROC_C, null, null,
                null, date.minusDays(3), date);

        // 4
        ControllerQueryParams queryParams4 = new ControllerQueryParams(DLN_WROC_C, null, null, null,
                date.plusDays(1), null);

        // 5
        Housing singleHousing = generateHousing(HousingType.FLAT, BigDecimal.valueOf(567899.99), 75, 2,
                Region.M_WAW_W, date.minusDays(2));
        ControllerQueryParams queryParams5 = new ControllerQueryParams(Region.M_WAW_W, HousingSize.M, 2,
                List.of(HousingType.FLAT, HousingType.TERRACED_HOUSE), date.minusDays(3), date);

        // 6
        ControllerQueryParams queryParams6 = new ControllerQueryParams(DLN_WROC_C, HousingSize.M, 2,
                List.of(HousingType.FLAT, HousingType.TERRACED_HOUSE), date.minusDays(3), date);

        return Stream.of(
                Arguments.of(List.of(housing, housing2, housing3, housing4), queryParams,
                        price1.add(price2).divide(BigDecimal.valueOf(2))),
                Arguments.of(List.of(housing, housing2, housing3, housing4, housing5), queryParams2,
                        price4),
                Arguments.of(List.of(housing, housing2, housing3, housing4, housing5), queryParams3,
                        price1.add(price2).add(price3).add(price4).divide(BigDecimal.valueOf(4))),
                Arguments.of(List.of(housing, housing2, housing3, housing4, housing5), queryParams4,
                        BigDecimal.ZERO),
                Arguments.of(List.of(singleHousing), queryParams5, singleHousing.getPrice()),
                Arguments.of(List.of(), queryParams6, BigDecimal.ZERO)
        );
    }

    private record ControllerQueryParams(Region region, HousingSize size, Integer rooms,
                                         List<HousingType> types, LocalDate dateSince, LocalDate dateUntil) {
    }
}