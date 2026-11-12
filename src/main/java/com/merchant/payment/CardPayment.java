package com.merchant.payment;

import java.util.Locale;


public class CardPayment implements PaymentMethod {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing Card Payment for R" + String.format(Locale.ROOT, "%.2f", amount));
        return true;
    }

    @Override
    public String getPaymentType() {
        return "CARD";
    }
}