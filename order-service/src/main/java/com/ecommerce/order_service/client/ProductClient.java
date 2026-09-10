package com.ecommerce.order_service.client;

import com.ecommerce.order_service.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class ProductClient {

    private final RestClient restClient;

    public ProductClient(
            @Qualifier("loadBalancedRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public ProductResponse getProduct(Long productId) {

        return restClient.get()
                .uri(
                        "http://product-service/api/products/{id}",
                        productId
                )
                .retrieve()
                .body(ProductResponse.class);
    }
}