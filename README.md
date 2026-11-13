# Merchant Engine

A small order-desk and catalogue engine: customers place orders made up of
products drawn from a catalogue, the total is calculated for them, and the
order is paid through a choice of payment rails (card, EFT, wallet and
buy-now-pay-later).

## Project structure

```
src/main/java/com/merchant/
  app/       PayNestApplication.java   - entry point, wires everything together
  domain/    Customer, Product, Order, OrderItem, ProductCatalogue
  payment/   PaymentMethod, Refundable, PaymentProcessor,
             CardPayment, EftPayment, WalletPayment, BuyNowPayLaterPayment
  service/   OrderService

src/test/java/com/merchant/
  domain/    ProductTest, OrderTest, ProductCatalogueTest
  payment/   PaymentMethodTest
  service/   OrderServiceTest
```

## Design decisions

**Domain vs. service vs. presentation**
`Order`, `Product`, `OrderItem`, `Customer` and `ProductCatalogue` hold only
data and the rules that protect their own consistency (e.g. an `Order` won't
accept a negative quantity). They know nothing about printing the order
summary. The one exception is `Order#checkout`, which prints the completion
line because the Capstone 3 brief asks for it.

`OrderService` orchestrates operations that need more than one domain object
at once - for example, adding a product to an order by looking it up in the
catalogue, so callers never need to reach into the catalogue's internals
directly - and owns printing the order summary, so those `System.out` calls
stay out of the domain layer.

`PayNestApplication.main()` only wires objects together. It builds the
catalogue and customer, then loops over every payment rail, creating an order
and checking it out for each one. It has no business or formatting logic of
its own.

**Payments: interfaces and polymorphism**
I made `PaymentMethod` an interface so `Order` never has to know which payment
rail it is dealing with. Every rail promises two things:
`processPayment(double amount)` and `getPaymentType()`. `Order#checkout` works
out the total once and hands that same amount to whatever `PaymentMethod` it
is given, so the amount charged always matches `calculateTotal()`.

*Why interfaces beat one big method.* If checkout was one big method full of
`if (type == "CARD") ... else if (type == "EFT") ...`, I would have to open it
and edit it every time a new rail was added, and each edit could break a rail
that already works. The same messages would also be repeated in every branch.
With an interface, each rail (`CardPayment`, `EftPayment`, `WalletPayment`,
`BuyNowPayLaterPayment`) looks after itself and `Order` only knows the
contract. To add a new rail I write one new class and add it to the list in
the demo. I did exactly that with `BuyNowPayLaterPayment`, and `Order` did not
change.

`PaymentProcessor` takes any `PaymentMethod` and an amount, runs the payment
and prints success or failure, so those messages are written once for every
rail.

**Refunds: a second, smaller interface**
Not every rail can give money back, so I made `Refundable` a separate
interface instead of forcing a refund method onto every rail. `CardPayment`
and `WalletPayment` implement it. `EftPayment` and `BuyNowPayLaterPayment` do
not. `Order#refundableCheckout` only accepts a `Refundable`, so Java enforces
the rule and there is no `if` on the rail type. In the demo I use
`instanceof` and a cast to ask a rail "can you refund?". That asks about an
ability, not about one specific rail.

**Wallet: a rule of its own**
Not every rule belongs in the shared code. `PaymentProcessor` refuses any
amount of R0 or less for every rail, but only a wallet has a balance, so
`WalletPayment` enforces its own rule: it holds a balance, declines a payment
bigger than that balance (printing "insufficient funds") and returns `false`
so the order is not completed. A successful payment takes the amount out of
the balance, and a refund puts it back. A negative starting balance is
rejected in the constructor. `Order` does not know any of this, because it
only sees a `PaymentMethod` that answered `true` or `false`.

**Validation**
All domain constructors and mutating methods (`Order.addItem`,
`ProductCatalogue.addProduct`) validate fail-fast: invalid input throws
`IllegalArgumentException` immediately rather than being silently ignored or
printed to the console.

Checkout and payments work the same way. A missing payment method throws
`IllegalArgumentException`, and checking out an empty order throws
`IllegalStateException`. Amounts of R0 or less are refused for both payments
and refunds. I put those amount checks in `PaymentProcessor`, so every rail is
protected by one copy and none of them repeats that check. Rules that belong
to one rail only, like the wallet balance, live inside that rail.

**Money**
Prices and totals use `BigDecimal` instead of `double`, since floating point
binary types are not safe for currency arithmetic (repeated addition of
`double` values can drift by fractions of a cent). `PaymentMethod` takes a
`double` because the Capstone 3 brief says so, so `Order#checkout` converts
the `BigDecimal` total with `doubleValue()` at that one point.

**Encapsulation**
`Order.getItems()` and `ProductCatalogue.getProducts()` return
`Collections.unmodifiableList(...)` views. Callers can read the list but
can't mutate the domain object's internal state through the getter.

**Equality**
`Product.equals()`/`hashCode()` are based on product ID, so products
representing "the same catalogue entry" compare equal even if two `Product`
instances were constructed separately (useful once products can be
looked up, merged, or de-duplicated).

## Running the app

Requires Java 21 and Maven.

```
mvn compile exec:java
```

This runs `com.merchant.app.PayNestApplication`. It builds a small catalogue
and a customer, then creates one order for each payment rail (card, EFT,
wallet and buy-now-pay-later). For each order it prints the summary and total,
then pays for it. Card and wallet go through the refundable checkout. The demo
wallet starts with R20000, which is enough to pay the R15900.00 order.

To try a different set of rails, change the list in `PayNestApplication`:

```java
List<PaymentMethod> methods = List.of(new CardPayment(), new EftPayment(),
        new WalletPayment(20000), new BuyNowPayLaterPayment());
```

## Running the tests

```
mvn test
```

Test coverage includes:
- Line and grand totals across single and multiple items
- Empty order behaviour (total of zero, empty item list)
- Duplicate products added as separate order lines
- Fail-fast validation: zero/negative quantity, null product, negative
  price, blank name, invalid IDs, null customer
- Unmodifiable list getters (`Order.getItems()`, `ProductCatalogue.getProducts()`)
- Catalogue lookup by ID, including the not-found case
- `OrderService` orchestration (adding a looked-up product to an order,
  rejecting an unknown product ID)
- Checkout with card, EFT, wallet and buy-now-pay-later through one list of
  `PaymentMethod`
- The amount charged equals the order total, using a fake rail that records
  the amount it receives
- Failed payments make checkout fail
- Rejection of a missing payment method, an empty order, and amounts of R0 or
  less (payments and refunds)
- Refunds for card and wallet, and EFT confirmed as not refundable
- A R500 wallet declines a R600 order and keeps its money
- Wallet balance goes down after a payment and back up after a refund