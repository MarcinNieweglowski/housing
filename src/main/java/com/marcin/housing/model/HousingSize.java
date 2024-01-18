package com.marcin.housing.model;

import lombok.Getter;

@Getter
public enum HousingSize {
    S(18, 45),
    M(46, 80),
    L(81, 400);

    private int minArea;
    private int maxArea;

    HousingSize(int minArea, int maxArea) {
        this.minArea = minArea;
        this.maxArea = maxArea;
    }

}
