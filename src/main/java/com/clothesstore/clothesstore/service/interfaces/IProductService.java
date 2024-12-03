package com.clothesstore.clothesstore.service.interfaces;

import com.clothesstore.clothesstore.persistence.entity.Product;
import com.clothesstore.clothesstore.presentation.dto.ProductDTO;

public interface IProductService {
    Product create(ProductDTO productDTO);
}
