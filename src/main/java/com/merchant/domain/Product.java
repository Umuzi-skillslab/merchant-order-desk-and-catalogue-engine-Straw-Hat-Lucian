package com.merchant.domain;

import java.math.BigDecimal;
import java.util.Objects;

// Product Class
public class Product {
    private final int id;
    private final String name;
    private final BigDecimal price;

    // Constructor
    public Product(int id, String name, BigDecimal price) {
        if (id <= 0) {
            throw new IllegalArgumentException("Product ID must be greater than 0");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
        if (price.signum() < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.id = id;
        this.name = name;
        this.price = price;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    // Used for comparing product IDs instead of memory addresses
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Product)) {
            return false;
        }
        Product other = (Product) obj;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
