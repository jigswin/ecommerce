package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.client.ProductClient;
import com.ecommerce.inventory_service.dto.InventoryRequest;
import com.ecommerce.inventory_service.dto.InventoryResponse;
import com.ecommerce.inventory_service.entity.Inventory;
import com.ecommerce.inventory_service.exception.InvalidQuantityException;
import com.ecommerce.inventory_service.exception.InventoryAlreadyExistsException;
import com.ecommerce.inventory_service.mapper.InventoryMapper;
import com.ecommerce.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ecommerce.inventory_service.exception.InventoryNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import com.ecommerce.inventory_service.exception.InsufficientStockException;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final ProductClient productClient;

    public InventoryResponse createInventory(InventoryRequest request) {

        // check from product service
        if (!productClient.productExists(request.getProductId())) {
            throw new InventoryNotFoundException(
                    "Product not found: " + request.getProductId()
            );
        }


        if (inventoryRepository.existsByProductId(request.getProductId())) {
            throw new InventoryAlreadyExistsException(
                    "Inventory already exists for product: "
                            + request.getProductId()
            );
        }

        Inventory inventory = inventoryMapper.toEntity(request);

        Inventory savedInventory = inventoryRepository.save(inventory);

        return inventoryMapper.toResponse(savedInventory);
    }

    public InventoryResponse getInventoryByProductId(Long productId) {

        Inventory inventory = inventoryRepository
                .findByProductId(productId)
                .orElseThrow(() ->
                        new InventoryNotFoundException(
                                "Inventory not found for product: " + productId
                        )
                );

        return inventoryMapper.toResponse(inventory);
    }

    public InventoryResponse updateInventory(
            Long productId,
            InventoryRequest request) {

        Inventory inventory = inventoryRepository
                .findByProductId(productId)
                .orElseThrow(() ->
                        new InventoryNotFoundException(
                                "Inventory not found for product: " + productId
                        )
                );

        inventory.setAvailableQuantity(request.getAvailableQuantity());
        inventory.setReservedQuantity(request.getReservedQuantity());

        Inventory updatedInventory =
                inventoryRepository.save(inventory);

        return inventoryMapper.toResponse(updatedInventory);
    }

    public void deleteInventory(Long productId) {

        Inventory inventory = inventoryRepository
                .findByProductId(productId)
                .orElseThrow(() ->
                        new InventoryNotFoundException(
                                "Inventory not found for product: " + productId
                        )
                );

        inventoryRepository.delete(inventory);
    }

    // check stock
    public boolean checkStock(Long productId, Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new InvalidQuantityException(
                    "Quantity must be greater than zero"
            );
        }

        Inventory inventory = inventoryRepository
                .findByProductId(productId)
                .orElseThrow(() ->
                        new InventoryNotFoundException(
                                "Inventory not found for product: " + productId
                        )
                );

        return inventory.getAvailableQuantity() >= quantity;
    }


    // reserve stock
    @Transactional
    public void reserveStock(Long productId, Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new InvalidQuantityException(
                    "Quantity must be greater than zero"
            );
        }

        Inventory inventory = inventoryRepository
                .findByProductId(productId)
                .orElseThrow(() ->
                        new InventoryNotFoundException(
                                "Inventory not found for product: " + productId
                        )
                );

        if (inventory.getAvailableQuantity() < quantity) {
            throw new InsufficientStockException(
                    "Insufficient stock for product: " + productId
            );
        }

        inventory.setAvailableQuantity(
                inventory.getAvailableQuantity() - quantity
        );

        inventory.setReservedQuantity(
                inventory.getReservedQuantity() + quantity
        );

        inventoryRepository.save(inventory);
    }

    // release stock
    @Transactional
    public void releaseStock(Long productId, Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new InvalidQuantityException(
                    "Quantity must be greater than zero"
            );
        }

        Inventory inventory = inventoryRepository
                .findByProductId(productId)
                .orElseThrow(() ->
                        new InventoryNotFoundException(
                                "Inventory not found for product: " + productId
                        )
                );

        if (inventory.getReservedQuantity() < quantity) {
            throw new InsufficientStockException(
                    "Reserved stock is insufficient for product: " + productId
            );
        }

        inventory.setReservedQuantity(
                inventory.getReservedQuantity() - quantity
        );

        inventory.setAvailableQuantity(
                inventory.getAvailableQuantity() + quantity
        );

        inventoryRepository.save(inventory);
    }
}