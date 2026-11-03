
package com.inventory_concurrency.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    @Test
    void constructor_shouldCreateInventorySuccessfully() {
        Inventory inventory = new Inventory(1L, "Keyboard", 10);

        assertEquals(1L, inventory.getProductId());
        assertEquals("Keyboard", inventory.getProductName());
        assertEquals(10, inventory.getQuantity());
    }

    @Test
    void constructor_shouldRejectNullProductId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Inventory(null, "Keyboard", 10));
    }

    @Test
    void constructor_shouldRejectBlankProductName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Inventory(1L, " ", 10));
    }

    @Test
    void constructor_shouldRejectNegativeQuantity() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Inventory(1L, "Keyboard", -1));
    }

    @Test
    void stockIn_shouldIncreaseQuantity() {
        Inventory inventory = new Inventory(1L, "Keyboard", 10);

        inventory.stockIn(5);

        assertEquals(15, inventory.getQuantity());
    }

    @Test
    void stockIn_shouldRejectZeroQuantity() {
        Inventory inventory = new Inventory(1L, "Keyboard", 10);

        assertThrows(
                IllegalArgumentException.class,
                () -> inventory.stockIn(0));
    }

    @Test
    void stockIn_shouldRejectNegativeQuantity() {
        Inventory inventory = new Inventory(1L, "Keyboard", 10);

        assertThrows(
                IllegalArgumentException.class,
                () -> inventory.stockIn(-5));
    }

    @Test
    void updateProduct_shouldUpdateNameAndQuantity() {
        Inventory inventory = new Inventory(1L, "Keyboard", 10);

        inventory.updateProduct("Keyboard Pro", 20);

        assertEquals("Keyboard Pro", inventory.getProductName());
        assertEquals(20, inventory.getQuantity());
    }

    @Test
    void updateProduct_shouldRejectBlankName() {
        Inventory inventory = new Inventory(1L, "Keyboard", 10);

        assertThrows(
                IllegalArgumentException.class,
                () -> inventory.updateProduct(" ", 20));
    }

    @Test
    void updateProduct_shouldRejectNegativeQuantity() {
        Inventory inventory = new Inventory(1L, "Keyboard", 10);

        assertThrows(
                IllegalArgumentException.class,
                () -> inventory.updateProduct("Keyboard Pro", -1));
    }

    @Test
    void stockIn_shouldThrowExceptionWhenQuantityOverflows() {
        Inventory inventory = new Inventory(
                1L, "Keyboard", Integer.MAX_VALUE);

        assertThrows(
                ArithmeticException.class,
                () -> inventory.stockIn(1));
    }
}
