package com.marcin.housing.service;

import com.marcin.housing.model.Housing;
import com.marcin.housing.model.HousingSize;
import com.marcin.housing.model.HousingType;
import com.marcin.housing.model.Region;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class HousingSpecification {

    public static Specification<Housing> hasRegion(Region region) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("region"), region);
    }

    public static Specification<Housing> hasRoomsEqualTo(int rooms) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("rooms"), String.valueOf(rooms));
    }

    public static Specification<Housing> hasSizeBetween(HousingSize size) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("area"), size.getMinArea(), size.getMaxArea());
    }

    public static Specification<Housing> hasTypesIn(List<HousingType> types) {
        return (root, query, criteriaBuilder) -> {
            In<HousingType> inClause = criteriaBuilder.in(root.get("type"));
            types.stream().forEach(type -> inClause.value(type));
            return inClause;
        };
    }

    public static Specification<Housing> hasDateAfter(LocalDate dateSince) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("dateAdded"), dateSince);
    }

    public static Specification<Housing> hasDateBefore(LocalDate dateUntil) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("dateAdded"), dateUntil);
    }
}
