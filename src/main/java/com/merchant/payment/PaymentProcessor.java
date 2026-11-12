package com.merchant.payment;

import java.util.Locale;

public class PaymentProcessor {
    public boolean processPayment(PaymentMethod method, double amount) {
        if (method == null){
            throw new IllegalArgumentException("Payment cannot be null");
        }
        if (amount <= 0) {
            System.out.println("Payment refused: the amount must be greater than R0");
            return false;
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

    public boolean refundPayment(Refundable payment, double amount) {
        if (payment == null){
            throw new IllegalArgumentException("Refund payment cannot be null");
        }
        if (amount <= 0) {
            System.out.println("Refund refused: the amount must be greater than R0");
            return  false;
        }

        boolean success = payment.refund(amount);
        if (success){
            System.out.println("Refund successful via " + payment.getPaymentType());
        } else  {
            System.out.println("Refund failed via " + payment.getPaymentType());
        }
        return success;
    }
}
