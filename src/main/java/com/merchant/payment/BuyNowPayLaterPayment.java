package com.merchant.payment;

import java.util.Locale;

public class BuyNowPayLaterPayment implements PaymentMethod{
    @Override
    public boolean processPayment(double amount){
        System.out.println("Processing buy-now-pay-later plan for R" + String.format(Locale.ROOT,"%.2f", amount));
        return true;
    }

    @Override
    public String getPaymentType(){
        return "BNPL";
    }

}
