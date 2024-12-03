package com.clothesstore.clothesstore.persistence.entity;

public enum Country {

    COLOMBIA(50),
    MEXICO(50),
    CHILE(30),
    PERU(30);

    private final int maxDiscount;

    Country(int maxDiscount) {
        this.maxDiscount = maxDiscount;

    }

    public int getMaxDiscount() {
        return maxDiscount;
    }


}
