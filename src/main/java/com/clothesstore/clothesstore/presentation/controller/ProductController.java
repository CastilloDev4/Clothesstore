package com.clothesstore.clothesstore.presentation.controller;

import com.clothesstore.clothesstore.persistence.entity.Product;
import com.clothesstore.clothesstore.presentation.dto.ProductDTO;
import com.clothesstore.clothesstore.service.interfaces.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {


    private final IProductService productService;

    @Autowired
    public ProductController(IProductService productService) {
        this.productService = productService;
    }
    @PostMapping("/save")
    public ResponseEntity<Product> create(@Valid @RequestBody ProductDTO productDTO) {
        Product product = productService.saveProduct(productDTO);
        return ResponseEntity.ok(product);
    }



}
