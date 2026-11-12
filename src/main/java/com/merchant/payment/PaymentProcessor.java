package com.merchant.payment;

import java.util.Locale;

public class PaymentProcessor {
    public boolean processPayment(PaymentMethod method, double amount) {
        if (method == null){
            throw new IllegalArgumentException("Payment cannot be null");
        }

        boolean success = method.processPayment(amount);

        if (success){
            System.out.println("Payment successful via " + method.getPaymentType());
            System.out.println("Amount: R" + String.format(Locale.ROOT,"%.2f", amount));
        } else {
            System.out.println("Payment failed via " + method.getPaymentType());
        }
        return  success;
    }
}
