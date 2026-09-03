package com.ecommerce.product_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/* @Entity
→ Java class ko DB table banata hai

@Table(name = "products")
→ Table ka naam products

@Id
→ Primary Key

@GeneratedValue
→ ID automatically generate

@Column(nullable = false)
→ Value required hai

@Getter/@Setter
→ Lombok getters/setters

@Builder
→ Object easily create karne ke liye

DB me roughly:

products
--------------------------------
id | name | price | quantity
--------------------------------
1  | iPhone | 79999 | 10
2  | Laptop | 55000 | 5

 */

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    private Integer quantity;
}