package com.merchant.payment;


// The contract every payment rail follows, so Order never needs to know which rail it is using
public interface PaymentMethod {
    boolean processPayment(double amount);
    String getPaymentType();
}