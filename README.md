# Merchant Engine

A small order-desk and catalogue engine: customers place orders made up of
products drawn from a catalogue, and the total is calculated for them.

## Project structure

```
src/main/java/com/merchant/
  app/       PayNestApplication.java   - entry point, wires everything together
  domain/    Customer, Product, Order, OrderItem, ProductCatalogue
  service/   OrderService, OrderReceiptFormatter

src/test/java/com/merchant/
  domain/    ProductTest, OrderTest, ProductCatalogueTest
  service/   OrderServiceTest
```

## Design decisions

**Domain vs. service vs. presentation**
`Order`, `Product`, `OrderItem`, `Customer` and `ProductCatalogue` hold only
data and the rules that protect their own consistency (e.g. an `Order` won't
accept a negative quantity). They know nothing about printing.

`OrderService` orchestrates operations that need more than one domain object
at once - for example, adding a product to an order by looking it up in the
catalogue, so callers never need to reach into the catalogue's internals
directly.

`OrderReceiptFormatter` turns an `Order` into a printable string. This keeps
`System.out` calls out of the domain layer and out of `main`, so the
formatting can change (or be replaced with, say, an HTML export) without
touching any business logic.

`PayNestApplication.main()` only wires objects together and prints the
result - it has no business logic or formatting logic of its own.

**Validation**
All domain constructors and mutating methods (`Order.addItem`,
`ProductCatalogue.addProduct`) validate fail-fast: invalid input throws
`IllegalArgumentException` immediately rather than being silently ignored or
printed to the console.

**Money**
Prices and totals use `BigDecimal` instead of `double`, since floating point
binary types are not safe for currency arithmetic (repeated addition of
`double` values can drift by fractions of a cent).

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

```
mvn exec:java
```

This runs `com.merchant.app.PayNestApplication`, which builds a small
catalogue, a customer, and an order, then prints an order summary and total.

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
