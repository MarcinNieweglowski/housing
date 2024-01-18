package com.marcin.housing.mapper;

import com.marcin.housing.model.Housing;
import com.marcin.housing.model.Region;
import com.marcin.housing.service.HousingClient.HousingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HousingMapper {


    @Mapping(target = "dateAdded", expression = "java(java.time.LocalDate.now())")
    @Mapping(source = "region", target = "region")
    public abstract Housing housingResponseToHousing(HousingResponse housingResponse, Region region);
}
