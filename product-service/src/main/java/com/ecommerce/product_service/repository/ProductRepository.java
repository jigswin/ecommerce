package com.ecommerce.product_service.repository;

import com.ecommerce.product_service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;


public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

    Page<Product> findByNameContainingIgnoreCaseAndPriceLessThanEqual(
            String name,
            BigDecimal maxPrice,
            Pageable pageable
    );

    Page<Product> findByPriceLessThanEqual(
            BigDecimal maxPrice,
            Pageable pageable
    );

    Page<Product> findByPriceGreaterThanEqual(
            BigDecimal minPrice,
            Pageable pageable
    );

    Page<Product> findByNameContainingIgnoreCaseAndPriceGreaterThanEqual(
            String name,
            BigDecimal minPrice,
            Pageable pageable
    );

    Page<Product> findByNameContainingIgnoreCaseAndPriceBetween(
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );
}


/*
🧠 Yaha ek important concept revise:

Product
   ↓
@Entity
   ↓
Database Table

ProductRepository
   ↓
JpaRepository
   ↓
CRUD operations



-> JpaRepository<Product, Long> ka matlab:

Product → kis entity ke liye repository hai
Long → Product ka ID type

Aur sabse important: hume save(), findById(), findAll(), deleteById() manually nahi likhne padte. Spring Data JPA provide karta hai.
 */