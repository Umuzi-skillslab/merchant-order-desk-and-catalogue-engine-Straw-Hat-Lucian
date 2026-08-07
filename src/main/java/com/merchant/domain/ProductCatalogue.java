package com.merchant.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ProductCatalogue {

    private final List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        products.add(product);
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    // Decouples product lookup from callers - they ask for a product by id
    // instead of reaching into the raw product list themselves.
    public Optional<Product> findById(int productId) {
        return products.stream()
                .filter(p -> p.getId() == productId)
                .findFirst();
    }
}
