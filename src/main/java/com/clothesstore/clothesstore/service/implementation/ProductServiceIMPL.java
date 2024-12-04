package com.clothesstore.clothesstore.service.implementation;

import com.clothesstore.clothesstore.persistence.builders.ProductBuilder;
import com.clothesstore.clothesstore.persistence.entity.Country;
import com.clothesstore.clothesstore.persistence.entity.Product;
import com.clothesstore.clothesstore.persistence.repository.IProductRepository;
import com.clothesstore.clothesstore.presentation.dto.ProductDTO;
import com.clothesstore.clothesstore.service.exception.DiscountException;
import com.clothesstore.clothesstore.service.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceIMPL implements IProductService {

    private final IProductRepository iProductRepository;
    @Autowired
    public ProductServiceIMPL(IProductRepository iProductRepository) {
        this.iProductRepository = iProductRepository;
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
        //Guardo el producto
        return iProductRepository.save(product);
    }
    private void validateDiscount(Integer discount, Country country) {
        if (discount > country.getMaxDiscount()) {
            throw new DiscountException("El descuento para "+ country + " no puede ser mayor al "+ country.getMaxDiscount() + "%");
        }
    }
}
