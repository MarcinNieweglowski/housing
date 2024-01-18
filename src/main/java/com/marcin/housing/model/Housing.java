package com.marcin.housing.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Housing {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private HousingType type;

    private BigDecimal price;

    private String description;

    private double area;

    private int rooms;

    @Enumerated(EnumType.STRING)
    private Region region;

    private LocalDate dateAdded;
}
