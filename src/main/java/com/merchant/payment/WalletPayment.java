package com.merchant.payment;

import java.util.Locale;

public class WalletPayment implements PaymentMethod, Refundable {

    private double balance;

    public WalletPayment(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Wallet balance cannot be negative");
        }
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public boolean processPayment(double amount) {

        if (amount > balance) {
            System.out.println("Wallet payment declined: insufficient funds (balance R" + String.format(Locale.ROOT, "%.2f", balance) + ")");
            return false;
        }

        System.out.println("Processing in-app wallet payment for R" + String.format(Locale.ROOT, "%.2f", amount));
        balance -= amount;
        return true;
    }

    @Override
    public String getPaymentType() {
        return "WALLET";
    }

    @Override
    public boolean refund(double amount) {
        System.out.println("Refunding R" + String.format(Locale.ROOT, "%.2f", amount) + " to the in-app wallet");
        balance += amount;
        return true;
    }
}