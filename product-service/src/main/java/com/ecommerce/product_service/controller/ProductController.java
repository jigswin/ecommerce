
/*

🧠 Ab poora flow samajh

For example:

POST /api/products

Request:

{
    "name": "iPhone 17",
    "price": 79999,
    "quantity": 10
}

Flow:

Postman
   ↓
ProductController
   ↓
ProductService
   ↓
ProductRepository
   ↓
JPA/Hibernate
   ↓
MySQL


Ek-ek annotation ka interview meaning

@RestController
Class REST API requests handle karegi aur response generally JSON me degi.

@RequestMapping("/api/products")
Common URL prefix.

So:

@GetMapping

becomes:

GET /api/products

and:

@GetMapping("/{id}")

becomes:

GET /api/products/1

@RequestBody

JSON request ko Java Product object me convert karta hai.

@PathVariable

URL se ID nikalta hai:

/api/products/10
              ↑
             id

ResponseEntity

Hum HTTP status + response body dono control kar sakte hain.
 */

package com.ecommerce.product_service.controller;

import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.product_service.dto.ProductRequest;
import jakarta.validation.Valid;
import com.ecommerce.product_service.dto.ProductResponse;
import java.math.BigDecimal;


@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Create Product
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {

        ProductResponse createdProduct = productService.createProduct(request);

        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    // Get All Products
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Pageable pageable) {

        Page<ProductResponse> products =
                productService.getAllProducts(name, minPrice, maxPrice, pageable);

        return ResponseEntity.ok(products);
    }

    // Get Product By ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable Long id) {

        ProductResponse product =
                productService.getProductById(id);

        return ResponseEntity.ok(product);
    }

    // Update Product
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {

        ProductResponse updatedProduct = productService.updateProduct(id, request);

        return ResponseEntity.ok(updatedProduct);
    }

    // Delete Product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
}