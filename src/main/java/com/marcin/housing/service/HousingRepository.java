package com.marcin.housing.service;

import com.marcin.housing.model.Housing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HousingRepository extends JpaRepository<Housing, UUID>, JpaSpecificationExecutor<Housing> {
}
