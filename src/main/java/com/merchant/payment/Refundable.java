package com.merchant.payment;

public interface Refundable extends PaymentMethod {
    boolean refund(double amount);
}