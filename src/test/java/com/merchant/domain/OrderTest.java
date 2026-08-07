package com.merchant.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Customer customer() {
        return new Customer(1, "Lucian", "lucian@email.com");
    }

    @Test
    void shouldCalculateTotalForSingleItem() {
        Product laptop = new Product(1, "Laptop", new BigDecimal("1000"));
        Order order = new Order(1, customer());

        order.addItem(laptop, 2);

        assertEquals(new BigDecimal("2000"), order.calculateTotal());
    }

    @Test
    void shouldCalculateTotalAcrossMultipleItems() {
        Product laptop = new Product(1, "Laptop", new BigDecimal("1000"));
        Product keyboard = new Product(2, "Keyboard", new BigDecimal("50"));
        Product mouse = new Product(3, "Mouse", new BigDecimal("25"));
        Order order = new Order(1, customer());

        order.addItem(laptop, 1);
        order.addItem(keyboard, 2);
        order.addItem(mouse, 1);

        assertEquals(new BigDecimal("1125"), order.calculateTotal());
    }

    @Test
    void shouldReturnZeroTotalForEmptyOrder() {
        Order order = new Order(1, customer());

        assertEquals(BigDecimal.ZERO, order.calculateTotal());
        assertTrue(order.getItems().isEmpty());
    }

    @Test
    void shouldAllowDuplicateProductAddedAsSeparateLines() {
        Product mouse = new Product(3, "Mouse", new BigDecimal("25"));
        Order order = new Order(1, customer());

        order.addItem(mouse, 1);
        order.addItem(mouse, 1);

        assertEquals(2, order.getItems().size());
        assertEquals(new BigDecimal("50"), order.calculateTotal());
    }

    @Test
    void shouldRejectZeroQuantity() {
        Product laptop = new Product(1, "Laptop", new BigDecimal("1000"));
        Order order = new Order(1, customer());

        assertThrows(IllegalArgumentException.class, () -> order.addItem(laptop, 0));
    }

    @Test
    void shouldRejectNegativeQuantity() {
        Product laptop = new Product(1, "Laptop", new BigDecimal("1000"));
        Order order = new Order(1, customer());

        assertThrows(IllegalArgumentException.class, () -> order.addItem(laptop, -1));
    }

    @Test
    void shouldRejectNullProduct() {
        Order order = new Order(1, customer());

        assertThrows(IllegalArgumentException.class, () -> order.addItem(null, 1));
    }

    @Test
    void shouldRejectNullCustomer() {
        assertThrows(IllegalArgumentException.class, () -> new Order(1, null));
    }

    @Test
    void shouldRejectZeroOrNegativeOrderId() {
        assertThrows(IllegalArgumentException.class, () -> new Order(0, customer()));
    }

    @Test
    void itemsListShouldBeUnmodifiable() {
        Product laptop = new Product(1, "Laptop", new BigDecimal("1000"));
        Order order = new Order(1, customer());
        order.addItem(laptop, 1);

        assertThrows(UnsupportedOperationException.class,
                () -> order.getItems().add(new OrderItem(laptop, 1)));
    }
}
