/*

### Global Exception Handling — Spring Boot

`@RestControllerAdvice` ka use application ke **saare Controllers ki exceptions ko centrally handle** karne ke liye hota hai.

`@ExceptionHandler(Exception.class)` batata hai ki **specific exception aane par kaunsa method execute hoga**.

Example flow:

```text
Controller
   ↓
Service
   ↓
Exception thrown
   ↓
@RestControllerAdvice
   ↓
@ExceptionHandler
   ↓
Custom Error Response
```

Example:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProductNotFound(
            ProductNotFoundException exception) {

        Map<String, Object> response = new HashMap<>();

        response.put("status", 404);
        response.put("message", exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
```

Agar `ProductNotFoundException` throw hoti hai:

```text
ProductNotFoundException
        ↓
@ExceptionHandler
        ↓
handleProductNotFound()
```

Response:

```json
{
    "status": 404,
    "message": "Product not found with id: 1"
}
```

**Simple Interview Answer:**

`@RestControllerAdvice` provides centralized exception handling for REST controllers, while `@ExceptionHandler`
defines how a specific exception should be handled and what response should be returned.

 */

package com.ecommerce.product_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(
            ProductNotFoundException exception) {

        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /*
        exception
           ↓
        getBindingResult()
           ↓
        Validation Result
           ↓
        name   → Product name is required
        price  → Price must be greater than 0
     */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .errors(errors)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}