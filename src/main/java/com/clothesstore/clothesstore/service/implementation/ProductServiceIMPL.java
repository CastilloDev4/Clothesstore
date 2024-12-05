package com.clothesstore.clothesstore.service.implementation;

import com.clothesstore.clothesstore.persistence.builders.ProductBuilder;
import com.clothesstore.clothesstore.persistence.entity.Country;
import com.clothesstore.clothesstore.persistence.entity.Image;
import com.clothesstore.clothesstore.persistence.entity.Product;
import com.clothesstore.clothesstore.persistence.repository.IProductRepository;
import com.clothesstore.clothesstore.presentation.dto.ProductDTO;
import com.clothesstore.clothesstore.service.exception.DiscountException;
import com.clothesstore.clothesstore.service.exception.DuplicateNameException;
import com.clothesstore.clothesstore.service.exception.FieldEmptyException;
import com.clothesstore.clothesstore.service.exception.NegativeValueException;
import com.clothesstore.clothesstore.service.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceIMPL implements IProductService {

    private final S3Service s3Service;

    private final IProductRepository productRepository;
    @Autowired
    public ProductServiceIMPL(S3Service s3Service, IProductRepository iProductRepository)
    {
        this.s3Service = s3Service;
        this.productRepository = iProductRepository;
    }


    @Transactional
    @Override
    public Product saveProduct(ProductDTO productDTO) {


        //VALIDACIONES ANTES DE CREAR EL PRODUCTO

        validateDiscount(productDTO.getDiscount(), productDTO.getCountry());
        if (
                productDTO.getName() == null ||
                productDTO.getName().isEmpty() ||
                productDTO.getDescription() == null ||
                productDTO.getDescription().isEmpty() ||
                productDTO.getCountry() == null
        )
        {
            throw new FieldEmptyException("No pueden haber campos vacíos");
        }

        //campos precio y descuento
        if(
                productDTO.getPrice() <= 0)
        {
            throw new NegativeValueException("El precio debe ser mayor a 0");
        }

        if(
                productDTO.getDiscount() < 0)
        {
            throw new NegativeValueException("El descuento debe ser mayor o igual a 0");
        }

        //si existe con el mismo nombre
        if(productRepository.existsByName(productDTO.getName()))
        {
            throw new DuplicateNameException("El producto "+ productDTO.getName() + " ya existe");
        }


        //Creo el producto
        Product product = new ProductBuilder()

            .name(productDTO.getName())
            .description(productDTO.getDescription())
            .price(productDTO.getPrice())
            .discount(productDTO.getDiscount())
            .country(productDTO.getCountry())
            .build();

        List<Image> images;
        try {
            images = s3Service.handleImageUpload(productDTO);
        } catch (IOException e) {

            throw new RuntimeException("Error al manejar la carga de las imagenes: " + e.getMessage(), e);
        }
        for (Image image : images) {
            image.setProduct(product); // Asigno el producto a la imagen
            product.addImage(image); // Asigno la imagen al producto
        }

        //Guardo el producto
        return productRepository.save(product);
    }
    //validacion de descuento por pais
    private void validateDiscount(Integer discount, Country country) {
        if (discount > country.getMaxDiscount()) {
            throw new DiscountException("El descuento para "+ country + " no puede ser mayor al "+ country.getMaxDiscount() + "%");
        }
    }
}
