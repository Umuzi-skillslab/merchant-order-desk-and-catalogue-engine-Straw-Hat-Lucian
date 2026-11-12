package com.merchant.payment;

import com.merchant.domain.Customer;
import com.merchant.domain.Order;
import com.merchant.domain.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMethodTest {

    // Builds the same order as the demo, so each test starts fresh
    private Order buildOrder() {
        Order order = new Order(1, new Customer(1, "Lucian", "lucian@email.com"));
        order.addItem(new Product(1, "Laptop", new BigDecimal("15000.00")), 1);
        order.addItem(new Product(2, "Keyboard", new BigDecimal("300.00")), 2);
        order.addItem(new Product(3, "Mouse", new BigDecimal("300.00")), 1);
        return order;
    }

    @Test
    void checkoutWorksWithEveryPaymentType() {
        List<PaymentMethod> methods = List.of(new CardPayment(), new EftPayment(), new WalletPayment(), new BuyNowPayLaterPayment());

        for (PaymentMethod method : methods) {
            assertTrue(buildOrder().checkout(method));
        }
    }

    // A fake payment type used only in tests. It remembers the amount it was asked to charge.
    private static class RecordingPayment implements PaymentMethod {
        double amountReceived;

        @Override
        public boolean processPayment(double amount) {
            amountReceived = amount;
            return true;
        }

        @Override
        public String getPaymentType() {
            return "TEST";
        }
    }

    @Test
    void amountChargedEqualsOrderTotal() {
        Order order = buildOrder();
        RecordingPayment recorder = new RecordingPayment();

        order.checkout(recorder);

        assertEquals(order.calculateTotal().doubleValue(), recorder.amountReceived, 0.001);
    }
    // A fake payment type that always fails, used to test the failure path
    private static class FailingPayment implements PaymentMethod {
        @Override
        public boolean processPayment(double amount) {
            return false;
        }

        @Override
        public String getPaymentType() {
            return "FAILING";
        }
    }

    @Test
    void checkoutRejectsMissingPaymentMethod() {
        assertThrows(IllegalArgumentException.class, () -> buildOrder().checkout(null));
    }

    @Test
    void checkoutRejectsEmptyOrder() {
        Order emptyOrder = new Order(2, new Customer(1, "Lucian", "lucian@email.com"));

        assertThrows(IllegalStateException.class, () -> emptyOrder.checkout(new CardPayment()));
    }

    @Test
    void failedPaymentMeansCheckoutFails() {
        assertFalse(buildOrder().checkout(new FailingPayment()));
    }
    @Test
    void processorRefusesZeroAmount() {
        PaymentProcessor processor = new PaymentProcessor();

        assertFalse(processor.processPayment(new CardPayment(), 0));
    }

    @Test
    void processorRefusesNegativeAmount() {
        PaymentProcessor processor = new PaymentProcessor();

        assertFalse(processor.processPayment(new WalletPayment(), -50));
    }

    @Test
    void processorRejectsMissingPaymentMethod() {
        PaymentProcessor processor = new PaymentProcessor();

        assertThrows(IllegalArgumentException.class, () -> processor.processPayment(null, 100));
    }
    @Test
    void cardAndWalletCanBeRefunded() {
        PaymentProcessor processor = new PaymentProcessor();

        assertTrue(processor.refundPayment(new CardPayment(), 100));
        assertTrue(processor.refundPayment(new WalletPayment(), 100));
    }

    @Test
    void processorRefusesZeroRefund() {
        PaymentProcessor processor = new PaymentProcessor();

        assertFalse(processor.refundPayment(new CardPayment(), 0));
    }

    @Test
    void processorRejectsMissingRefundablePayment() {
        PaymentProcessor processor = new PaymentProcessor();

        assertThrows(IllegalArgumentException.class, () -> processor.refundPayment(null, 100));
    }

    @Test
    void eftIsNotRefundable() {
        PaymentMethod eft = new EftPayment();

        assertFalse(eft instanceof Refundable);
    }

    @Test
    void refundableCheckoutWorksForCard() {
        assertTrue(buildOrder().refundableCheckout(new CardPayment()));
    }
}