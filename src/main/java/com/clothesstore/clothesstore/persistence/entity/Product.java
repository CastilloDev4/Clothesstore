package com.clothesstore.clothesstore.persistence.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "productos", uniqueConstraints = {@UniqueConstraint(columnNames = {"nombre"})})
public class Product {
    @Id
    @Column(name = "producto_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    @NotBlank(message = "El nombre del producto no puede estar vacio")
    private String name;

    @Column(name = "descripcion", nullable = false, length = 255)
    @NotBlank(message = "La descripcion del producto no puede estar vacia")
    private String description;

    @Column(name = "precio", nullable = false)
    @NotNull(message = "El precio no puede estar vacio")
    @Positive(message = "El precio debe ser mayor a 0")
    private double price;

    @Column(name = "descuento", nullable = false)
    @NotNull(message = "El descuento no puede estar vacio")
    @Min(value = 0, message = "El descuento debe ser mayor o igual a 0")
    private int discount;

    @Column(name = "precio_descuento", nullable = false)
    @JsonIgnore
    private double discountPrice;

    @Column(name = "busquedas", nullable = false)
    @JsonIgnore
    private int searchCount = 0;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonBackReference
    private List<Image> productImage = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "Pais", nullable = false,length = 50)
    @NotNull(message = "El pais no puede estar vacio")
    private Country country;


    public Long getId() {
        return id;
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
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getDiscount() {
        return discount;
    }
    public void setDiscount(int discount) {
        this.discount = discount;
    }
    public Country getCountry() {
        return country;
    }
    public void setCountry(Country country) {
        this.country = country;
    }
    public List<Image> getProductImage() {
        return productImage;

    }

    public double getDiscountPrice() {
        return discountPrice;
    }
    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getSearchCount() {
        return searchCount;
    }
    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }



    public void setProductImage(List<Image> productImage) {
        this.productImage = productImage;
    }

    public void addImage(Image image) {
        this.productImage.add(image);
        image.setProduct(this);
    }

}
