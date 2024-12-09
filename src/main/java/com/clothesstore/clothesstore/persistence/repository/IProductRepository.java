package com.clothesstore.clothesstore.persistence.repository;

import com.clothesstore.clothesstore.persistence.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductRepository extends CrudRepository<Product, Long> {

    Optional<Product> findByName(String name);
    @Query(value = "SELECT * FROM productos WHERE busquedas > 0 ORDER BY busquedas DESC LIMIT 5", nativeQuery = true)
    List<Product> findMostSearchedProducts();
    boolean existsByName(String name);
    Optional<Product> findById(Long id);
    void deleteById(Long id);




}
