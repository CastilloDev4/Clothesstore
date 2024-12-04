package com.clothesstore.clothesstore.service.implementation;

import com.clothesstore.clothesstore.persistence.builders.ProductBuilder;
import com.clothesstore.clothesstore.persistence.entity.Country;
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

@Service
public class ProductServiceIMPL implements IProductService {

    private final IProductRepository productRepository;
    @Autowired
    public ProductServiceIMPL(IProductRepository iProductRepository) {
        this.productRepository = iProductRepository;
    }



    @Override
    public Product saveProduct(ProductDTO productDTO) {
        validateDiscount(productDTO.getDiscount(), productDTO.getCountry());

        //Creo el producto

        Product product = new ProductBuilder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .discount(productDTO.getDiscount())
                .country(productDTO.getCountry())
                .build();
        //validaciones
        if (
                product.getName() == null ||
                product.getName().isEmpty() ||
                product.getDescription() == null ||
                product.getDescription().isEmpty() ||
                product.getCountry() == null)
        {
            throw new FieldEmptyException("No pueden haber campos vacíos");
        }
        if(
            product.getPrice() <= 0)
        {
            throw new NegativeValueException("El precio debe ser mayor a 0");
        }

        if(
                product.getDiscount() < 0)
        {
            throw new NegativeValueException("El descuento debe ser mayor o igual a 0");
        }

        if(productRepository.existsByName(product.getName()))
        {
            throw new DuplicateNameException("El producto "+ product.getName() + " ya existe");
        }

        //Guardo el producto
        return productRepository.save(product);
    }
    private void validateDiscount(Integer discount, Country country) {
        if (discount > country.getMaxDiscount()) {
            throw new DiscountException("El descuento para "+ country + " no puede ser mayor al "+ country.getMaxDiscount() + "%");
        }
    }
}
