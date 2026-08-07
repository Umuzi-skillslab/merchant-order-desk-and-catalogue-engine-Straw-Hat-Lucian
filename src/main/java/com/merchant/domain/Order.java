package com.merchant.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
    // Attributes
    private final int id;
    private final Customer customer;
    private final List<OrderItem> items;

    // Constructor
    public Order(int id, Customer customer) {
        if (id <= 0) {
            throw new IllegalArgumentException("Order ID must be greater than 0");
        }
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        this.id = id;
        this.customer = customer;
        this.items = new ArrayList<>();
    }

    // Add an item to the order. Fails fast instead of silently ignoring bad input.
    public void addItem(Product product, int quantity) {
        items.add(new OrderItem(product, quantity));
    }

    // Calculate the grand total for the order
    public BigDecimal calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : items) {
            total = total.add(item.getLineTotal());
        }
        return total;
    }

    // Getters
    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}
