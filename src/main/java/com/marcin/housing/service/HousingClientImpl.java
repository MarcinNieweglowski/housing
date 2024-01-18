package com.marcin.housing.service;

import com.marcin.housing.mapper.HousingMapper;
import com.marcin.housing.model.Housing;
import com.marcin.housing.model.Region;
import com.marcin.housing.service.HousingClient.HousingDataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class HousingClientImpl {

    @Autowired
    private HousingClient housingClient;

    @Autowired
    private HousingMapper mapper;

    public List<Housing> fetchHousingData(Region regionID) {
        log.info("About to fetch data for regionID: '{}'", regionID);
        int pageNo = 1;

        List<HousingDataResponse> responseList = populateResponseList(new ArrayList<>(), regionID, pageNo);;

        return responseList.stream()
                .map(HousingDataResponse::data)
                .flatMap(List::stream)
                .map(housingResponse -> mapper.housingResponseToHousing(housingResponse, regionID))
                .toList();
    }

    private List<HousingDataResponse> populateResponseList(List<HousingDataResponse> responseList, Region regionID, int pageNo) {
        HousingDataResponse housingDataResponse = housingClient.getHousingDataResponse(regionID, pageNo);
        responseList.add(housingDataResponse);
        pageNo++;

        if (pageNo > housingDataResponse.totalPages()) {
            return responseList;
        }

        return populateResponseList(responseList, regionID, pageNo);
    }
}
