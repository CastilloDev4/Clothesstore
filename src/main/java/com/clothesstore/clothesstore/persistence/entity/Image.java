package com.clothesstore.clothesstore.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Setter
@Getter
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

    @Column(name = "url", nullable = false, length = 255)
    @NotBlank(message = "La url de la imagen no puede estar vacia")
    private String url;


    @Column(name = "tamaño", nullable = false)
    @NotNull(message = "El tamaño de la imagen no puede estar vacio")
    private Long size;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Product product;
}
