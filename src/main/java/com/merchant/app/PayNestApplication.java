package com.merchant.app;

import java.math.BigDecimal;

import com.merchant.domain.Customer;
import com.merchant.domain.Order;
import com.merchant.domain.Product;
import com.merchant.domain.ProductCatalogue;
import com.merchant.service.OrderReceiptFormatter;
import com.merchant.service.OrderService;

public class PayNestApplication {

    public static void main(String[] args) {

        // Create products
        Product laptop = new Product(1, "Laptop", new BigDecimal("15000.00"));
        Product keyboard = new Product(2, "Keyboard", new BigDecimal("300.00"));
        Product mouse = new Product(3, "Mouse", new BigDecimal("300.00"));

        ProductCatalogue catalogue = new ProductCatalogue();
        catalogue.addProduct(laptop);
        catalogue.addProduct(keyboard);
        catalogue.addProduct(mouse);

        // Create customer
        Customer customer = new Customer(1, "Lucian", "lucian@email.com");

        // Create order
        Order order = new Order(1, customer);

        OrderService service = new OrderService();

        // Add items to order via the catalogue, through the service layer
        service.addProductToOrder(order, catalogue, laptop.getId(), 1);
        service.addProductToOrder(order, catalogue, keyboard.getId(), 2);
        service.addProductToOrder(order, catalogue, mouse.getId(), 1);

        BigDecimal total = service.calculateOrderTotal(order);

        OrderReceiptFormatter formatter = new OrderReceiptFormatter();
        System.out.println(formatter.format(order, total));
    }
}
