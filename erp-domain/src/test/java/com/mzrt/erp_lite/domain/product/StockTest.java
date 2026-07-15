package com.mzrt.erp_lite.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Stock DomainTest")
class StockTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidation {

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void shouldThrowIllegalArgumentExceptionWhenValueIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Stock(null));

            assertEquals("Stock cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is negative")
        void shouldThrowIllegalArgumentExceptionWhenValueIsNegative() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Stock(-1));

            assertEquals("Stock cannot be negative", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Factory methods")
    class FactoryMethods {

        @Test
        @DisplayName("Should create Stock exposing the provided value")
        void shouldCreateStockWithProvidedValue() {
            Stock stock = Stock.of(10);

            assertEquals(10, stock.value());
        }

        @Test
        @DisplayName("Should create Stock with zero value")
        void shouldCreateStockWithZeroValue() {
            Stock stock = Stock.zero();

            assertEquals(0, stock.value());
        }
    }

    @Nested
    @DisplayName("increment")
    class Increment {

        @Test
        @DisplayName("Should increment stock by the given quantity")
        void shouldIncrementStockByGivenQuantity() {
            Stock stock = Stock.of(10);

            Stock result = stock.increment(5);

            assertEquals(15, result.value());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when increment quantity is negative")
        void shouldThrowIllegalArgumentExceptionWhenIncrementQuantityIsNegative() {
            Stock stock = Stock.of(10);

            assertThrows(IllegalArgumentException.class, () -> stock.increment(-1));
        }
    }

    @Nested
    @DisplayName("decrement")
    class Decrement {

        @Test
        @DisplayName("Should decrement stock by the given quantity")
        void shouldDecrementStockByGivenQuantity() {
            Stock stock = Stock.of(10);

            Stock result = stock.decrement(4);

            assertEquals(6, result.value());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when decrement quantity is negative")
        void shouldThrowIllegalArgumentExceptionWhenDecrementQuantityIsNegative() {
            Stock stock = Stock.of(10);

            assertThrows(IllegalArgumentException.class, () -> stock.decrement(-1));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when decrementing below zero")
        void shouldThrowIllegalArgumentExceptionWhenDecrementingBelowZero() {
            Stock stock = Stock.of(5);

            assertThrows(IllegalArgumentException.class, () -> stock.decrement(6));
        }

        @Test
        @DisplayName("Should allow decrementing exactly to zero")
        void shouldAllowDecrementingExactlyToZero() {
            Stock stock = Stock.of(5);

            Stock result = stock.decrement(5);

            assertEquals(0, result.value());
        }
    }

    @Nested
    @DisplayName("hasAvailable")
    class HasAvailable {

        @Test
        @DisplayName("Should return true when stock is greater than the required quantity")
        void shouldReturnTrueWhenStockIsGreaterThanRequired() {
            Stock stock = Stock.of(10);

            assertTrue(stock.hasAvailable(5));
        }

        @Test
        @DisplayName("Should return true when stock exactly matches the required quantity")
        void shouldReturnTrueWhenStockMatchesRequired() {
            Stock stock = Stock.of(5);

            assertTrue(stock.hasAvailable(5));
        }

        @Test
        @DisplayName("Should return false when stock is less than the required quantity")
        void shouldReturnFalseWhenStockIsLessThanRequired() {
            Stock stock = Stock.of(3);

            assertFalse(stock.hasAvailable(5));
        }
    }
}
