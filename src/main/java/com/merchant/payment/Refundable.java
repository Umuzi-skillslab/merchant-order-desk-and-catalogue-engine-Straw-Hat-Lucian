package com.merchant.payment;

// An extra ability for rails that can give money back (card and wallet, but not EFT)
public interface Refundable extends PaymentMethod {
    boolean refund(double amount);
}