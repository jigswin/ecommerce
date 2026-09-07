package com.ecommerce.inventory_service.client;

import com.ecommerce.inventory_service.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class ProductClient {

    private final RestClient restClient;

    public boolean productExists(Long productId) {

        try {

            restClient.get()
                    .uri(
                            "http://localhost:8081/api/products/{id}",
                            productId
                    )
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is4xxClientError,
                            (request, response) -> {
                                throw new ProductNotFoundException(
                                        "Product not found: " + productId
                                );
                            }
                    )
                    .toBodilessEntity();

            return true;

        } catch (ProductNotFoundException exception) {

            return false;
        }
    }
}