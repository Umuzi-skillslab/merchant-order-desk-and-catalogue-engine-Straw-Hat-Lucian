package com.merchant.payment;

import java.util.Locale;

public class WalletPayment implements PaymentMethod, Refundable {

    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing in-app wallet payment for R" + String.format(Locale.ROOT, "%.2f", amount));
        return true;
    }

    @Override
    public String getPaymentType() {
        return "WALLET";
    }

    @Override
    public boolean refund(double amount) {
        System.out.println("Refunding R" + String.format(Locale.ROOT, "%.2f", amount) + " to the in-app wallet");
        return true;
    }
}