package com.merchant.payment;

public interface PaymentMethod {
    boolean processPayment(double amount);
    String getPaymentType();
}