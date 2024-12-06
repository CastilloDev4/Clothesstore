package com.clothesstore.clothesstore.presentation.controller;

import com.clothesstore.clothesstore.persistence.entity.Product;
import com.clothesstore.clothesstore.presentation.dto.MostSearchedProductDTO;
import com.clothesstore.clothesstore.presentation.dto.ProductDTO;
import com.clothesstore.clothesstore.service.implementation.S3Service;
import com.clothesstore.clothesstore.service.interfaces.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*")
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

            // se guarda el producto con las imagenes
            Product product = productService.saveProduct(productDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(product);

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getByIdProduct(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<?> getByNameProduct(@PathVariable String name) {
        return productService.findByName(name)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/getTopSearched")
    public ResponseEntity<?> getTopSearched() {
        List<MostSearchedProductDTO> mostSearchedProductDTOS = productService.getMostSearchedProducts();
        return ResponseEntity.ok(mostSearchedProductDTOS);

    }

}