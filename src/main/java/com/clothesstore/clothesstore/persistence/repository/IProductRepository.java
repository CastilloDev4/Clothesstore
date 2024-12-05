package com.clothesstore.clothesstore.persistence.repository;

import com.clothesstore.clothesstore.persistence.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductRepository extends CrudRepository<Product, Long> {

    Optional<Product> findByName(String name);
    List<Product> findTop10ByOrderByPriceDesc();
    boolean existsByName(String name);
    Optional<Product> findById(Long id);



}
