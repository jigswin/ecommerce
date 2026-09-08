# E-Commerce Microservices

A production-style E-Commerce application built using **Java 21, Spring Boot, Spring Data JPA, MySQL and Microservices Architecture**.

The project is being developed step-by-step to demonstrate real-world microservices concepts such as service discovery, inter-service communication, load balancing, database separation and business workflows.

## Services

* Product Service
* Inventory Service
* Order Service
* Eureka Server

## Technology Stack

* Java 21
* Spring Boot 4.1.1
* Spring Data JPA
* MySQL
* Maven
* REST API
* Spring Cloud Netflix Eureka
* Spring Cloud LoadBalancer
* RestClient
* Lombok
* Bean Validation

## Architecture

```text
                    ┌──────────────────┐
                    │   Eureka Server  │
                    │      :8761       │
                    └────────┬─────────┘
                             │
              ┌──────────────┼──────────────┐
              │              │              │
              ▼              ▼              ▼
        Product Service  Inventory Service  Order Service
             :8081             :8082            :8083
                                  │
                                  │
                           Service-to-Service
                            Communication
                                  │
                                  ▼
                         Spring Cloud
                           LoadBalancer
                                  │
                                  ▼
                              Eureka
```

## Current Progress

### Product Service

* Product CRUD
* Request/Response DTO
* Entity → DTO Mapper
* Bean Validation
* Global Exception Handling
* Pagination
* Sorting
* Search by Product Name
* Minimum Price Filtering
* Maximum Price Filtering
* Environment/Profile based configuration
* Database configuration using environment variables
* Product Service registered with Eureka

### Inventory Service

* Inventory CRUD
* Request/Response DTO
* Entity → DTO Mapper
* Bean Validation
* Global Exception Handling
* Duplicate Product Inventory Validation
* Stock Availability Check
* Stock Reservation
* Stock Release
* Transaction Management using `@Transactional`
* Product existence validation through Product Service
* Service-to-Service communication using RestClient
* Eureka Service Discovery
* Spring Cloud LoadBalancer
* Load-balanced RestClient
* Service-name based communication with Product Service
* Inventory Service registered with Eureka

### Order Service

* Order Service module created
* Eureka Service Discovery configured
* Order Service registered with Eureka
* Order Entity
* OrderItem Entity
* OrderStatus Enum
* One-to-Many / Many-to-One JPA relationship
* Request DTOs
* Response DTOs
* ProductClient for Product Service communication
* Load-balanced RestClient configuration
* Order Repository
* OrderItem Repository
* OrderService business layer skeleton

## Microservices Communication

The application uses **Eureka Service Discovery** instead of hardcoded service URLs.

Example:

```text
Order Service
      │
      ▼
ProductClient
      │
      ▼
http://product-service/api/products/{id}
      │
      ▼
Spring Cloud LoadBalancer
      │
      ▼
Eureka Server
      │
      ▼
Product Service
```

Inventory Service follows the same service-discovery approach when communicating with Product Service.

## JPA Entity Relationships

The Order Service uses the following relationship:

```text
Order
  │
  │ 1
  │
  │
  │ N
  ▼
OrderItem
```

One Order can contain multiple OrderItems.

Each OrderItem stores:

* Product ID
* Quantity
* Price snapshot
* Order reference

The order item price is stored at the time of ordering so that future product price changes do not affect historical orders.

## Upcoming

* Complete Order Creation Business Logic
* Order APIs
* Inventory Reservation during Order Creation
* Order Cancellation
* Order Status Management
* Payment Service
* Notification Service
* API Gateway
* Config Server
* Kafka
* Redis
* Docker
* Kubernetes
* AWS Deployment
* Centralized Logging
* Monitoring
* Distributed Tracing
* Saga Pattern
* System Design Documentation

## Project Goal

The goal of this project is to build a **production-style E-Commerce Microservices application** while implementing and understanding real-world backend concepts including:

* Microservices Architecture
* Service Discovery
* Inter-Service Communication
* Load Balancing
* REST APIs
* Database-per-Service approach
* Transaction Management
* Event-Driven Architecture
* Distributed Systems
* Containerization
* Cloud Deployment
* Monitoring and Observability
