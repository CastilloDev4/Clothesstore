package com.clothesstore.clothesstore.presentation.dto;

import com.clothesstore.clothesstore.persistence.entity.Country;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer discount;
    private Double discountPrice;
    private Country country;
    private List<ImageDTO> productImage = new ArrayList<>();

    public Long getId() {
        return id;}

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
    public Double getDiscountPrice() {
        return discountPrice;
    }
    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }
    public List<ImageDTO> getProductImage() {
        return productImage;
    }
    public void setProductImage(List<ImageDTO> productImage) {
        this.productImage = productImage;
    }
}
