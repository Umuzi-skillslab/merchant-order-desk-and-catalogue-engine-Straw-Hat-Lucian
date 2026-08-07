package com.merchant.service;

import com.merchant.domain.Order;
import com.merchant.domain.Product;
import com.merchant.domain.ProductCatalogue;

import java.math.BigDecimal;

// Orchestrates order-related operations that need more than one domain object
// at a time (e.g. looking a product up in a catalogue before adding it to an order).
public class OrderService {

    public BigDecimal calculateOrderTotal(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        return order.calculateTotal();
    }

    // Adds a product to the order by looking it up in the catalogue, so callers
    // don't need to depend on catalogue internals directly.
    public void addProductToOrder(Order order, ProductCatalogue catalogue, int productId, int quantity) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (catalogue == null) {
            throw new IllegalArgumentException("Catalogue cannot be null");
        }
        Product product = catalogue.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("No product found with id " + productId));
        order.addItem(product, quantity);
    }
}
