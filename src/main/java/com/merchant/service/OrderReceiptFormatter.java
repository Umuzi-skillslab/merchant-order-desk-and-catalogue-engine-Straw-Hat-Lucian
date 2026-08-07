package com.merchant.service;

import com.merchant.domain.Order;
import com.merchant.domain.OrderItem;

import java.math.BigDecimal;

// Turns an Order into a printable summary. Kept separate from Order itself so the
// domain class stays free of presentation concerns.
public class OrderReceiptFormatter {

    public String format(Order order, BigDecimal total) {
        StringBuilder sb = new StringBuilder();
        sb.append("=================================\n");
        sb.append("ORDER SUMMARY\n");
        sb.append("=================================\n");
        sb.append("Order ID: ").append(order.getId()).append("\n");
        sb.append("Customer: ").append(order.getCustomer().getName()).append("\n");

        for (OrderItem item : order.getItems()) {
            sb.append(item.getProduct().getName())
              .append(" x ")
              .append(item.getQuantity())
              .append(" = ")
              .append(item.getLineTotal())
              .append("\n");
        }

        sb.append("---------------------\n");
        sb.append("Total: R").append(total);

        return sb.toString();
    }
}
