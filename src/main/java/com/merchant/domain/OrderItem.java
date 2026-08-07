package com.merchant.domain;

import java.math.BigDecimal;

public class OrderItem {
    private final Product product;
    private final int quantity;

    // Constructor
    public OrderItem(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        this.product = product;
        this.quantity = quantity;
    }

    // Line total calculation
    public BigDecimal getLineTotal() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    // Getters
    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
