package com.merchant.app;

import com.merchant.domain.Customer;
import com.merchant.domain.Order;
import com.merchant.domain.Product;
import com.merchant.domain.ProductCatalogue;
import com.merchant.payment.*;
import com.merchant.service.OrderService;

import java.math.BigDecimal;
import java.util.List;

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

        OrderService service = new OrderService();

        // Every rail is typed as PaymentMethod, so the loop treats them all the same way
        List<PaymentMethod> methods = List.of(
                new CardPayment(),
                new EftPayment(),
                new WalletPayment(20000),
                new BuyNowPayLaterPayment());

        int orderId = 1;

        for (PaymentMethod method : methods) {
            Order freshOrder = new Order(orderId, customer);
            orderId++;

            service.addProductToOrder(freshOrder, catalogue, laptop.getId(), 1);
            service.addProductToOrder(freshOrder, catalogue, keyboard.getId(), 2);
            service.addProductToOrder(freshOrder, catalogue, mouse.getId(), 1);

            service.printOrderSummary(freshOrder);

            // Rails that can give money back use the refundable checkout
            if (method instanceof Refundable) {
                Refundable refundable = (Refundable) method;
                freshOrder.refundableCheckout(refundable);
            } else {
                freshOrder.checkout(method);
            }
        }

        System.out.println();
        System.out.println("--- Wallet with too little money ---");

        WalletPayment smallWallet = new WalletPayment(500);
        Order bigOrder = new Order(orderId, customer);
        service.addProductToOrder(bigOrder, catalogue, laptop.getId(), 1);
        bigOrder.checkout(smallWallet);
    }
}