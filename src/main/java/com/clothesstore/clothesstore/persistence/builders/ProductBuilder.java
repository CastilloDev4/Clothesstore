package com.clothesstore.clothesstore.persistence.builders;

import com.clothesstore.clothesstore.persistence.entity.Country;
import com.clothesstore.clothesstore.persistence.entity.Product;

public class ProductBuilder {
    private String name;
    private String description;
    private double price;
    private int discount;
    private Country country;



    public ProductBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder description(String description) {
        this.description = description;
        return this;
    }

    public ProductBuilder price(double price) {
        this.price = price;
        return this;
    }

    public ProductBuilder discount(int discount) {
        this.discount = discount;
        return this;
    }

    public ProductBuilder country(Country country) {
        this.country = country;
        return this;
    }

    public Product build() {
        Product product = new Product();
        product.setName(this.name);
        product.setDescription(this.description);
        product.setPrice(this.price);
        product.setDiscount(this.discount);
        product.setCountry(this.country);
        return product;
    }
}
