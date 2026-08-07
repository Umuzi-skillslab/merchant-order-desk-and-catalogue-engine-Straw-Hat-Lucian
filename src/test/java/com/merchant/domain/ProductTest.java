package com.merchant.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void shouldCreateProductWithValidData() {
        Product product = new Product(1, "Laptop", new BigDecimal("15000.00"));

        assertEquals(1, product.getId());
        assertEquals("Laptop", product.getName());
        assertEquals(new BigDecimal("15000.00"), product.getPrice());
    }

    @Test
    void shouldRejectZeroOrNegativeId() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product(0, "Laptop", BigDecimal.TEN));
    }

    @Test
    void shouldRejectBlankName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product(1, " ", BigDecimal.TEN));
    }

    @Test
    void shouldRejectNegativePrice() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product(1, "Laptop", new BigDecimal("-100")));
    }

    @Test
    void shouldAllowZeroPrice() {
        Product product = new Product(1, "Free Sample", BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, product.getPrice());
    }

    @Test
    void shouldConsiderProductsEqualById() {
        Product p1 = new Product(1, "Laptop", new BigDecimal("15000.00"));
        Product p2 = new Product(1, "Laptop - Refurbished", new BigDecimal("9000.00"));

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void shouldConsiderProductsWithDifferentIdsNotEqual() {
        Product p1 = new Product(1, "Laptop", new BigDecimal("15000.00"));
        Product p2 = new Product(2, "Laptop", new BigDecimal("15000.00"));

        assertNotEquals(p1, p2);
    }
}
