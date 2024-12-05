package com.c0324m4.finaltestm4.repository;

import com.c0324m4.finaltestm4.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContaining(String name);
    List<Product> findByPrice(Long price);
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByNameContainingAndPriceAndCategoryId(String name, Long price, Long categoryId);
}
