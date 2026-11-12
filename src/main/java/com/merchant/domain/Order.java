package com.merchant.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.merchant.payment.PaymentMethod;
import com.merchant.payment.PaymentProcessor;

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

    public boolean checkout(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Payment Method cannot be null");
        }
        if (items.isEmpty()) {
            throw new IllegalStateException("Cannot check out and empty order");
        }

        BigDecimal total = calculateTotal();

        PaymentProcessor processor = new PaymentProcessor();
        boolean success = processor.processPayment(paymentMethod, total.doubleValue());

        if (success) {
            System.out.println("Order #" + id + " completed. Paid via " + paymentMethod.getPaymentType() + ": R" + total);
        } else  {
            System.out.println("Order #" + id + " was NOT completed. Payment failed.");
        }

        return success;
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
