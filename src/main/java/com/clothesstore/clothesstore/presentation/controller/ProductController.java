package com.clothesstore.clothesstore.presentation.controller;

import com.clothesstore.clothesstore.persistence.entity.Product;
import com.clothesstore.clothesstore.presentation.dto.ImageDTO;
import com.clothesstore.clothesstore.presentation.dto.ProductDTO;
import com.clothesstore.clothesstore.service.implementation.S3Service;
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
    private final S3Service s3Service;


    @Autowired
    public ProductController(IProductService productService, S3Service s3Service) {
        this.productService = productService;
        this.s3Service = s3Service;
    }
    @PostMapping("/save")
    public ResponseEntity<?> create(@Valid @RequestBody ProductDTO productDTO) {
        try {
            for (ImageDTO imageDTO : productDTO.getProductImage()) {
                if (!s3Service.isImageSizeValid(imageDTO.getUrl())) {
                    return ResponseEntity.badRequest().body("La imagen con URL " + imageDTO.getUrl() + " supera 1 MB.");
                }
            }
            Product product = productService.saveProduct(productDTO);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al crear el producto: " + e.getMessage());
        }
    }}