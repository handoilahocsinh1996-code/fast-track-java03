
package com.inventory_concurrency.application.service;

import com.inventory_concurrency.application.dto.StockInRequest;
import com.inventory_concurrency.application.dto.UpdateInventoryRequest;
import com.inventory_concurrency.application.port.out.InventoryHistoryPort;
import com.inventory_concurrency.application.port.out.InventoryRepositoryPort;
import com.inventory_concurrency.domain.model.Inventory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepositoryPort inventoryRepository;

    @Mock
    private InventoryHistoryPort inventoryHistoryPort;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    void stockIn_shouldIncreaseQuantitySuccessfully() {
        // Arrange
        Long productId = 1L;
        Inventory inventory = mock(Inventory.class);

        when(inventoryRepository.findByIdForUpdate(productId))
                .thenReturn(Optional.of(inventory));

        when(inventoryRepository.save(inventory))
                .thenReturn(inventory);

        when(inventory.getProductId()).thenReturn(productId);
        when(inventory.getProductName()).thenReturn("Keyboard");
        when(inventory.getQuantity()).thenReturn(15);
        when(inventory.getVersion()).thenReturn(1L);

        StockInRequest request = new StockInRequest(5);

        // Act
        var response = inventoryService.stockIn(productId, request);

        // Assert
        verify(inventory).stockIn(5);
        verify(inventoryRepository).save(inventory);

        assertEquals(productId, response.productId());
        assertEquals("Keyboard", response.productName());
        assertEquals(15, response.quantity());
        assertEquals(1L, response.version());
    }

    @Test
    void stockIn_shouldThrowExceptionWhenInventoryNotFound() {
        // Arrange
        Long productId = 999L;

        when(inventoryRepository.findByIdForUpdate(productId))
                .thenReturn(Optional.empty());

        StockInRequest request = new StockInRequest(5);

        // Act & Assert
        assertThrows(
                InventoryNotFoundException.class,
                () -> inventoryService.stockIn(productId, request));

        verify(inventoryRepository, never()).save(any());
    }

    @Test
    void updateInventory_shouldUpdateSuccessfullyWhenVersionMatches() {
        // Arrange
        Long productId = 1L;
        Inventory inventory = mock(Inventory.class);

        when(inventoryRepository.findById(productId))
                .thenReturn(Optional.of(inventory));

        when(inventory.getVersion()).thenReturn(2L);
        when(inventoryRepository.saveAndFlush(inventory))
                .thenReturn(inventory);

        when(inventory.getProductId()).thenReturn(productId);
        when(inventory.getProductName()).thenReturn("Mechanical Keyboard");
        when(inventory.getQuantity()).thenReturn(20);

        UpdateInventoryRequest request = new UpdateInventoryRequest("Mechanical Keyboard", 20, 2L);

        // Act
        var response = inventoryService.updateInventory(productId, request);

        // Assert
        verify(inventory).updateProduct("Mechanical Keyboard", 20);
        verify(inventoryRepository).saveAndFlush(inventory);

        assertEquals("Mechanical Keyboard", response.productName());
        assertEquals(20, response.quantity());
    }

    @Test
    void updateInventory_shouldThrowConflictWhenVersionDoesNotMatch() {
        // Arrange
        Long productId = 1L;
        Inventory inventory = mock(Inventory.class);

        when(inventoryRepository.findById(productId))
                .thenReturn(Optional.of(inventory));

        when(inventory.getVersion()).thenReturn(3L);

        UpdateInventoryRequest request = new UpdateInventoryRequest("Mechanical Keyboard", 20, 2L);

        // Act & Assert
        assertThrows(
                InventoryVersionConflictException.class,
                () -> inventoryService.updateInventory(productId, request));

        verify(inventory, never()).updateProduct(anyString(), anyInt());
        verify(inventoryRepository, never()).saveAndFlush(any());
    }
}
