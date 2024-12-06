package com.clothesstore.clothesstore.service.interfaces;

import com.clothesstore.clothesstore.persistence.entity.Product;
import com.clothesstore.clothesstore.presentation.dto.MostSearchedProductDTO;
import com.clothesstore.clothesstore.presentation.dto.ProductDTO;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    Product save(ProductDTO productDTO);
    Optional<Product> findById(Long id);
    Optional<Product> findByName(String name);
    List<MostSearchedProductDTO> getMostSearchedProducts();


}
