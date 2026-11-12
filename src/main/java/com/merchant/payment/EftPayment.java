package com.merchant.payment;

import java.util.Locale;


public class EftPayment implements PaymentMethod{
    @Override
    public boolean processPayment(double amount){
        System.out.println("Processing EFT bank transfer for R" + String.format(Locale.ROOT,"%.2f", amount));
        return true;
    }

    @Override
    public String getPaymentType() {
        return "EFT";
    }
}
