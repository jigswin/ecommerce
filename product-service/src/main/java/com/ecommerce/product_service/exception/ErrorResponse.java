package com.ecommerce.product_service.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {

    private int status;
    private String message;
    private Map<String, String> errors;


}