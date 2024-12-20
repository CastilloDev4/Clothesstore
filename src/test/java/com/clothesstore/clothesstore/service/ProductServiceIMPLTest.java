package com.clothesstore.clothesstore.service;

import com.clothesstore.clothesstore.persistence.builders.ProductBuilder;
import com.clothesstore.clothesstore.persistence.entity.Country;
import com.clothesstore.clothesstore.persistence.entity.Product;
import com.clothesstore.clothesstore.persistence.repository.IProductRepository;
import com.clothesstore.clothesstore.presentation.dto.MostSearchedProductDTO;
import com.clothesstore.clothesstore.presentation.dto.ProductDTO;
import com.clothesstore.clothesstore.service.exception.DiscountException;
import com.clothesstore.clothesstore.service.exception.DuplicateNameException;
import com.clothesstore.clothesstore.service.exception.FieldEmptyException;
import com.clothesstore.clothesstore.service.implementation.ProductServiceIMPL;
import com.clothesstore.clothesstore.service.implementation.S3Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceIMPLTest {
    @Mock
    private S3Service s3Service;
    @Mock
    private IProductRepository productRepository;
    @InjectMocks
    private ProductServiceIMPL productServiceIMPL;


    @Test
    void testSaveProduct_Success() throws IOException {
        // Given
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Camisa");
        productDTO.setDescription("Camisa de vestir");
        productDTO.setPrice(120.000);
        productDTO.setDiscount(20);
        productDTO.setCountry(Country.COLOMBIA);


        Product product = new ProductBuilder()
                .name("Camisa")
                .description("Camisa de vestir")
                .price(120.000)
                .discount(20)
                .discountPrice(productDTO.getPrice() - (productDTO.getPrice() * productDTO.getDiscount() / 100))
                .country(productDTO.getCountry())
                .build();
        Mockito.when(productRepository.existsByName(productDTO.getName())).thenReturn(false);
        Mockito.when(s3Service.handleImageUpload(productDTO)).thenReturn(new ArrayList<>());
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);
        // When
        Product result = productServiceIMPL.save(productDTO);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(product.getName(), result.getName());
        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any(Product.class));

    }


    @Test
    void testSaveProduct_ThrowsFieldEmptyException() {
        // Given:
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("null");


        // When  Then:
        Assertions.assertThrows(FieldEmptyException.class, () -> productServiceIMPL.save(productDTO));
    }

    @Test
    void testSaveProduct_ThrowsDuplicateNameException() {
        // Given:
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("camisa");
        productDTO.setDescription("Camisa de vestir");
        productDTO.setPrice(120.000);
        productDTO.setDiscount(20);
        productDTO.setCountry(Country.COLOMBIA);

        //  producto con el mismo nombre ya existe en el repositorio
        Mockito.when(productRepository.existsByName(productDTO.getName())).thenReturn(true);

        // When and  Then
        Assertions.assertThrows(DuplicateNameException.class, () -> productServiceIMPL.save(productDTO));
    }
    @Test
    void TestSaveProduct_ThrowsDiscountException() {
        // Given:
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("camisa");
        productDTO.setDescription("Camisa de vestir");
        productDTO.setPrice(120.000);
        productDTO.setDiscount(120);
        productDTO.setCountry(Country.COLOMBIA);

        // When and Then
        Assertions.assertThrows(DiscountException.class, () -> productServiceIMPL.save(productDTO));
    }

    @Test
    void testFindById_Success() {
        Long id = 1L;
        // Given

        Product product = new ProductBuilder()

                .name("Camisa")
                .description("Camisa de vestir")
                .price(120.000)
                .discount(20)
                .discountPrice(96.000)
                .country(Country.COLOMBIA)
                .build();
        Mockito.when(productRepository.findById(id)).thenReturn(java.util.Optional.of(product));
        // When
        ProductDTO result = productServiceIMPL.findById(id).get();
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(product.getId(), result.getId());
        Mockito.verify(productRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void testFindById_NotFound() {
        // Given:
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<ProductDTO> result = productServiceIMPL.findById(1L);

        // Then:
        Assertions.assertFalse(result.isPresent());
    }


    @Test
    void testGetMostSearchedProducts_Success() {
        // Given:
        Product product = new Product();
        product.setName("Gorra");
        product.setPrice(100.0);
        product.setDiscount(10);
        product.setSearchCount(20);

        Mockito.when(productRepository.findMostSearchedProducts()).thenReturn(List.of(product));

        // When:
        List<MostSearchedProductDTO> result = productServiceIMPL.getMostSearchedProducts();

        // Then:
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(product.getName(), result.get(0).getName());
    }


    @Test
    void testGetMostSearchedProducts_Empty() {
        // Given:
        Mockito.when(productRepository.findMostSearchedProducts()).thenReturn(Collections.emptyList());

        // When and then
        Assertions.assertThrows(FieldEmptyException.class, productServiceIMPL::getMostSearchedProducts);
    }










}

















