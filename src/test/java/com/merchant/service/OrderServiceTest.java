package com.merchant.service;

import com.merchant.domain.Customer;
import com.merchant.domain.Order;
import com.merchant.domain.Product;
import com.merchant.domain.ProductCatalogue;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    private final OrderService service = new OrderService();

    private Order order() {
        return new Order(1, new Customer(1, "Lucian", "lucian@email.com"));
    }

    @Test
    void shouldCalculateOrderTotalThroughService() {
        Product laptop = new Product(1, "Laptop", new BigDecimal("1000"));
        Order order = order();
        order.addItem(laptop, 2);

        assertEquals(new BigDecimal("2000"), service.calculateOrderTotal(order));
    }

    @Test
    void shouldRejectNullOrderWhenCalculatingTotal() {
        assertThrows(IllegalArgumentException.class, () -> service.calculateOrderTotal(null));
    }

    @Test
    void shouldAddProductToOrderViaCatalogueLookup() {
        ProductCatalogue catalogue = new ProductCatalogue();
        Product laptop = new Product(1, "Laptop", new BigDecimal("1000"));
        catalogue.addProduct(laptop);
        Order order = order();

        service.addProductToOrder(order, catalogue, 1, 2);

        assertEquals(1, order.getItems().size());
        assertEquals(new BigDecimal("2000"), order.calculateTotal());
    }

    @Test
    void shouldThrowWhenProductIdNotInCatalogue() {
        ProductCatalogue catalogue = new ProductCatalogue();
        Order order = order();

        assertThrows(IllegalArgumentException.class,
                () -> service.addProductToOrder(order, catalogue, 99, 1));
    }
}
