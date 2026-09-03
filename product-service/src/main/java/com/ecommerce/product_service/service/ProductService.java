/*

@Service kyun?
@Service
public class ProductService

Spring ko bata raha hai:

"Ye class service/business-logic component hai."

Spring iska object automatically create karke IoC Container me rakhega.


🔥 Constructor Injection

Humne ye use kiya:

private final ProductRepository productRepository;

public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
}

Ye Dependency Injection hai.

Matlab ProductService ko ProductRepository chahiye, aur Spring automatically repository ka object provide karega.

Interview me bol sakta hai:

"I prefer constructor injection because dependencies are explicit, fields can be final, and it improves testability."
 */

package com.ecommerce.product_service.service;
import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.exception.ProductNotFoundException;
import com.ecommerce.product_service.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.ecommerce.product_service.dto.ProductRequest;
import com.ecommerce.product_service.dto.ProductResponse;
import com.ecommerce.product_service.mapper.ProductMapper;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductResponse createProduct(ProductRequest request) {

        Product product = productMapper.toEntity(request);

        Product savedProduct = productRepository.save(product);

        return productMapper.toResponse(savedProduct);
    }

    public Page<ProductResponse> getAllProducts(
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable) {

        Page<Product> products;

        // 1. Name + Min Price + Max Price
        if (name != null && !name.isBlank()
                && minPrice != null
                && maxPrice != null) {

            products = productRepository
                    .findByNameContainingIgnoreCaseAndPriceBetween(
                            name,
                            minPrice,
                            maxPrice,
                            pageable
                    );

        }

        // 2. Name + Min Price
        else if (name != null && !name.isBlank()
                && minPrice != null) {

            products = productRepository
                    .findByNameContainingIgnoreCaseAndPriceGreaterThanEqual(
                            name,
                            minPrice,
                            pageable
                    );

        }

        // 3. Name + Max Price
        else if (name != null && !name.isBlank()
                && maxPrice != null) {

            products = productRepository
                    .findByNameContainingIgnoreCaseAndPriceLessThanEqual(
                            name,
                            maxPrice,
                            pageable
                    );

        }

        // 4. Only Min Price
        else if (minPrice != null) {

            products = productRepository
                    .findByPriceGreaterThanEqual(
                            minPrice,
                            pageable
                    );

        }

        // 5. Only Max Price
        else if (maxPrice != null) {

            products = productRepository
                    .findByPriceLessThanEqual(
                            maxPrice,
                            pageable
                    );

        }

        // 6. Only Name
        else if (name != null && !name.isBlank()) {

            products = productRepository
                    .findByNameContainingIgnoreCase(
                            name,
                            pageable
                    );

        }

        // 7. No Filter
        else {

            products = productRepository.findAll(pageable);
        }

        return products.map(productMapper::toResponse);
    }
    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + id
                        )
                );

        return productMapper.toResponse(product);
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + id
                        )
                );

        existingProduct.setName(request.getName());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setQuantity(request.getQuantity());

        Product updatedProduct = productRepository.save(existingProduct);

        return productMapper.toResponse(updatedProduct);
    }


    public void deleteProduct(Long id) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + id
                        )
                );

        productRepository.delete(existingProduct);
    }
}