package com.inventory_concurrency.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import org.hibernate.envers.Audited;

@Entity
@Table(name = "inventories")
@Audited
public class Inventory {

    @Id
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_name", nullable = false, length = 150)
    private String productName;

    @Column(nullable = false)
    private int quantity;

    @Version
    @Column(nullable = false)
    private Long version;

    protected Inventory() {
        // Required by JPA
    }

    public Inventory(Long productId, String productName, int quantity) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID is required");
        }

        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getVersion() {
        return version;
    }

    public void updateProduct(String productName, int quantity) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        this.productName = productName;
        this.quantity = quantity;
    }

    public void stockIn(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Stock-in quantity must be greater than zero");
        }

        this.quantity = Math.addExact(this.quantity, amount);
    }
}