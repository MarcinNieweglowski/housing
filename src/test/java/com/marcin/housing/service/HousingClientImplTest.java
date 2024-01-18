package com.marcin.housing.service;

import com.marcin.housing.mapper.HousingMapper;
import com.marcin.housing.model.Housing;
import com.marcin.housing.model.Region;
import com.marcin.housing.service.HousingClient.HousingDataResponse;
import com.marcin.housing.service.HousingClient.HousingResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.marcin.housing.model.HousingType.FLAT;
import static com.marcin.housing.model.Region.*;
import static com.marcin.housing.utils.HousingGenerator.createHousingFromResponseData;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HousingClientImplTest {

    @Mock
    private HousingClient housingClient;

    @Mock
    private HousingMapper mapper;

    @InjectMocks
    private HousingClientImpl housingClientImpl;

    @Test
    void fetchHousingData_shouldCallHousingClient_toFetchAllPagesForGivenRegion() {
        HousingResponse housingResponse = new HousingResponse(UUID.randomUUID(), FLAT, "987654.11",
                "detailed description", "51.23", 3);
        List<HousingResponse> housingResponseList = List.of(housingResponse);
        HousingDataResponse housingDataResponse = new HousingDataResponse(5, housingResponseList);
        Housing mappedHousingData = createHousingFromResponseData(housingResponse, M_WAW_W);

        when(mapper.housingResponseToHousing(housingResponse, M_WAW_W)).thenReturn(mappedHousingData);
        when(housingClient.getHousingDataResponse(eq(M_WAW_W), anyInt())).thenReturn(housingDataResponse);

        List<Housing> housings = housingClientImpl.fetchHousingData(M_WAW_W);

        verify(housingClient, times(5)).getHousingDataResponse(eq(M_WAW_W), anyInt());

        assertThat(housings).satisfies(housingList -> {
            assertThat(housingList.size()).isEqualTo(5);
            Housing housing = housingList.get(0);
            assertThat(housing.getRegion()).isEqualTo(M_WAW_W);
            assertThat(housing.getDateAdded()).isEqualTo(LocalDate.now());
        });
    }

    @Test
    void fetchHousingData_shouldNotFail_ifNoDataWasReturnedForGivenRegion() {
        HousingDataResponse housingDataResponse = new HousingDataResponse(0, List.of());

        when(housingClient.getHousingDataResponse(DLN_WROC_C, 1)).thenReturn(housingDataResponse);

        List<Housing> housings = housingClientImpl.fetchHousingData(DLN_WROC_C);

        verify(housingClient).getHousingDataResponse(eq(DLN_WROC_C), anyInt());
        assertThat(housings.isEmpty()).isTrue();
    }

    @Test
    void fetchHousingData_shouldCallTheHousingMapper_forEachHousingResponse() {
        int pages = 5;
        HousingResponse housingResponse = new HousingResponse(UUID.randomUUID(), FLAT, "987654.11",
                "detailed description", "51.23", 3);
        List<HousingResponse> housingResponseList = List.of(housingResponse);
        HousingDataResponse housingDataResponse = new HousingDataResponse(pages, housingResponseList);

        when(housingClient.getHousingDataResponse(any(Region.class), anyInt())).thenReturn(housingDataResponse);
        Housing housingFromResponseData = createHousingFromResponseData(housingResponse, LUBL);
        when(mapper.housingResponseToHousing(housingResponse, LUBL)).thenReturn(housingFromResponseData);

        housingClientImpl.fetchHousingData(LUBL);

        verify(mapper, times(pages)).housingResponseToHousing(housingResponse, LUBL);
    }
}