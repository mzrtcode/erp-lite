package com.mzrt.erp_lite.domain.shared;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Money DomainTest")
class MoneyTest {

    private static final Currency USD = Currency.getInstance("USD");
    private static final Currency EUR = Currency.getInstance("EUR");

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidation {

        @Test
        @DisplayName("Should throw IllegalArgumentException when amount is null")
        void shouldThrowIllegalArgumentExceptionWhenAmountIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Money(null, USD));

            assertEquals("Amount cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when amount is negative")
        void shouldThrowIllegalArgumentExceptionWhenAmountIsNegative() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Money(BigDecimal.valueOf(-1), USD));

            assertEquals("Amount must be >= 0", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when currency is null")
        void shouldThrowIllegalArgumentExceptionWhenCurrencyIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Money(BigDecimal.TEN, null));

            assertEquals("Currency cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should accept a zero amount")
        void shouldAcceptZeroAmount() {
            Money money = new Money(BigDecimal.ZERO, USD);

            assertEquals(BigDecimal.ZERO, money.amount());
        }
    }

    @Nested
    @DisplayName("Factory methods")
    class FactoryMethods {

        @Test
        @DisplayName("Should create Money from a BigDecimal amount")
        void shouldCreateMoneyFromBigDecimalAmount() {
            Money money = Money.of(BigDecimal.TEN, USD);

            assertEquals(BigDecimal.TEN, money.amount());
            assertEquals(USD, money.currency());
        }

        @Test
        @DisplayName("Should create Money from a double amount")
        void shouldCreateMoneyFromDoubleAmount() {
            Money money = Money.of(10.5, USD);

            assertEquals(BigDecimal.valueOf(10.5), money.amount());
        }
    }

    @Nested
    @DisplayName("add")
    class Add {

        @Test
        @DisplayName("Should add two Money values with the same currency")
        void shouldAddTwoMoneyValuesWithSameCurrency() {
            Money first = Money.of(BigDecimal.TEN, USD);
            Money second = Money.of(BigDecimal.ONE, USD);

            Money result = first.add(second);

            assertEquals(BigDecimal.valueOf(11), result.amount());
            assertEquals(USD, result.currency());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when currencies differ")
        void shouldThrowIllegalArgumentExceptionWhenCurrenciesDiffer() {
            Money first = Money.of(BigDecimal.TEN, USD);
            Money second = Money.of(BigDecimal.ONE, EUR);

            assertThrows(IllegalArgumentException.class, () -> first.add(second));
        }
    }

    @Nested
    @DisplayName("subtract")
    class Subtract {

        @Test
        @DisplayName("Should subtract two Money values with the same currency")
        void shouldSubtractTwoMoneyValuesWithSameCurrency() {
            Money first = Money.of(BigDecimal.TEN, USD);
            Money second = Money.of(BigDecimal.ONE, USD);

            Money result = first.subtract(second);

            assertEquals(BigDecimal.valueOf(9), result.amount());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when the result would be negative")
        void shouldThrowIllegalArgumentExceptionWhenResultIsNegative() {
            Money first = Money.of(BigDecimal.ONE, USD);
            Money second = Money.of(BigDecimal.TEN, USD);

            assertThrows(IllegalArgumentException.class, () -> first.subtract(second));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when currencies differ")
        void shouldThrowIllegalArgumentExceptionWhenCurrenciesDiffer() {
            Money first = Money.of(BigDecimal.TEN, USD);
            Money second = Money.of(BigDecimal.ONE, EUR);

            assertThrows(IllegalArgumentException.class, () -> first.subtract(second));
        }
    }

    @Nested
    @DisplayName("multiply")
    class Multiply {

        @Test
        @DisplayName("Should multiply by an integer multiplier")
        void shouldMultiplyByIntegerMultiplier() {
            Money money = Money.of(BigDecimal.TEN, USD);

            Money result = money.multiply(3);

            assertEquals(BigDecimal.valueOf(30), result.amount());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when the integer multiplier is negative")
        void shouldThrowIllegalArgumentExceptionWhenIntegerMultiplierIsNegative() {
            Money money = Money.of(BigDecimal.TEN, USD);

            assertThrows(IllegalArgumentException.class, () -> money.multiply(-1));
        }

        @Test
        @DisplayName("Should multiply by a Quantity")
        void shouldMultiplyByQuantity() {
            Money money = Money.of(BigDecimal.TEN, USD);

            Money result = money.multiply(Quantity.of(3));

            assertEquals(BigDecimal.valueOf(30), result.amount());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when Quantity is null")
        void shouldThrowIllegalArgumentExceptionWhenQuantityIsNull() {
            Money money = Money.of(BigDecimal.TEN, USD);

            assertThrows(IllegalArgumentException.class, () -> money.multiply((Quantity) null));
        }
    }
}
