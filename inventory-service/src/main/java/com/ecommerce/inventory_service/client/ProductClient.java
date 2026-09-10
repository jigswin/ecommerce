package com.ecommerce.inventory_service.client;

import com.ecommerce.inventory_service.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ProductClient {

    private final RestClient restClient;

    public ProductClient(
            @Qualifier("loadBalancedRestClient")
            RestClient restClient) {

        this.restClient = restClient;
    }

    public boolean productExists(Long productId) {

        try {

            restClient.get()
                    .uri(
                            "http://product-service/api/products/{id}",
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