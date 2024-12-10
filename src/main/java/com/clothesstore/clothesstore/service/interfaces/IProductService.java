package com.clothesstore.clothesstore.service.interfaces;

import com.clothesstore.clothesstore.persistence.entity.Product;
import com.clothesstore.clothesstore.presentation.dto.MostSearchedProductDTO;
import com.clothesstore.clothesstore.presentation.dto.ProductDTO;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    Product save(ProductDTO productDTO);
    Optional<ProductDTO> findById(Long id);
    Optional<ProductDTO> findByName(String name);
    List<MostSearchedProductDTO> getMostSearchedProducts();
    void deleteById(Long id);
    Product update(Long id,ProductDTO productDTO);
    List<ProductDTO> findAll();
}
