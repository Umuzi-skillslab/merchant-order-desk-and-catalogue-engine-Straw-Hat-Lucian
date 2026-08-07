package com.merchant.domain;

// Customer Class
public class Customer {
    private final int id;
    private final String name;
    private final String email;

    // Constructor
    public Customer(int id, String name, String email) {
        if (id <= 0) {
            throw new IllegalArgumentException("Customer ID must be greater than 0");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
