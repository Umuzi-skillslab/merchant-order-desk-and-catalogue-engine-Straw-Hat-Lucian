package com.merchant.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductCatalogueTest {

    @Test
    void shouldFindProductById() {
        ProductCatalogue catalogue = new ProductCatalogue();
        Product laptop = new Product(1, "Laptop", new BigDecimal("1000"));
        catalogue.addProduct(laptop);

        Optional<Product> found = catalogue.findById(1);

        assertTrue(found.isPresent());
        assertEquals("Laptop", found.get().getName());
    }

    @Test
    void shouldReturnEmptyWhenProductNotFound() {
        ProductCatalogue catalogue = new ProductCatalogue();

        assertTrue(catalogue.findById(99).isEmpty());
    }

    @Test
    void shouldRejectNullProduct() {
        ProductCatalogue catalogue = new ProductCatalogue();

        assertThrows(IllegalArgumentException.class, () -> catalogue.addProduct(null));
    }

    @Test
    void productsListShouldBeUnmodifiable() {
        ProductCatalogue catalogue = new ProductCatalogue();
        Product laptop = new Product(1, "Laptop", new BigDecimal("1000"));
        catalogue.addProduct(laptop);

        assertThrows(UnsupportedOperationException.class,
                () -> catalogue.getProducts().add(laptop));
    }
}
