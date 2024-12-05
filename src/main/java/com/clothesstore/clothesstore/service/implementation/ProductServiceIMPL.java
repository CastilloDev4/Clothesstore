package com.clothesstore.clothesstore.service.implementation;

import com.clothesstore.clothesstore.persistence.builders.ImageBuilder;
import com.clothesstore.clothesstore.persistence.builders.ProductBuilder;
import com.clothesstore.clothesstore.persistence.entity.Country;
import com.clothesstore.clothesstore.persistence.entity.Image;
import com.clothesstore.clothesstore.persistence.entity.Product;
import com.clothesstore.clothesstore.persistence.repository.IProductRepository;
import com.clothesstore.clothesstore.presentation.dto.ImageDTO;
import com.clothesstore.clothesstore.presentation.dto.ProductDTO;
import com.clothesstore.clothesstore.service.exception.DiscountException;
import com.clothesstore.clothesstore.service.exception.DuplicateNameException;
import com.clothesstore.clothesstore.service.exception.FieldEmptyException;
import com.clothesstore.clothesstore.service.exception.NegativeValueException;
import com.clothesstore.clothesstore.service.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceIMPL implements IProductService {

    private final IProductRepository productRepository;
    @Autowired
    public ProductServiceIMPL(IProductRepository iProductRepository)
    {
        this.productRepository = iProductRepository;
    }



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

        for (ImageDTO imageDTO : productDTO.getProductImage()) {

            if (imageDTO.getUrl() == null || imageDTO.getUrl().isEmpty()) {
                throw new FieldEmptyException("La URL de la imagen no puede estar vacía");
            }
            if (imageDTO.getSize() <= 0) {
                throw new NegativeValueException("El tamaño de la imagen debe ser mayor a 0");
            }
            if (imageDTO.getDescriptionImage() == null || imageDTO.getDescriptionImage().isEmpty()) {
                throw new FieldEmptyException("La descripción de la imagen no puede estar vacía");
            }
            Image image = new ImageBuilder()
                    .url(imageDTO.getUrl())
                    .size(imageDTO.getSize())
                    .description(imageDTO.getDescriptionImage())
                    .product(product)
                    .build();
            // agrego las imagenes a la lista
            product.addImage(image);


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
