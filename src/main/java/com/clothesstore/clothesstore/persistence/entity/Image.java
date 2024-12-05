package com.clothesstore.clothesstore.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "imagenes")
public class Image {
//Creo imagen en otra entidad ya que necesitaré hacer la validacion de su tamaño en el servicio

    @Id
    @Column(name = "imagen_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "descripcion", nullable = false, length = 255)
    @NotBlank(message = "La descripcion de la imagen no puede estar vacia")
    private String descriptionImage;

    @Column(name = "url", nullable = false, length = 255)
    @NotBlank(message = "La url de la imagen no puede estar vacia")
    private String url;


    @Column(name = "tamaño", nullable = false)
    @NotNull(message = "El tamaño de la imagen no puede estar vacio")
    private Long size;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    @JsonBackReference
    private Product product;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getDescriptionImage() {
        return descriptionImage;
    }

    public void setDescriptionImage(String descriptionImage) {
        this.descriptionImage = descriptionImage;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
