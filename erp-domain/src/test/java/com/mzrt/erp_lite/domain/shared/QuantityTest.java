package com.mzrt.erp_lite.domain.shared;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Quantity DomainTest")
class QuantityTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidation {

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void shouldThrowIllegalArgumentExceptionWhenValueIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Quantity(null));

            assertEquals("Quantity cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is zero")
        void shouldThrowIllegalArgumentExceptionWhenValueIsZero() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Quantity(0));

            assertEquals("Quantity must be greater than 0", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is negative")
        void shouldThrowIllegalArgumentExceptionWhenValueIsNegative() {
            assertThrows(IllegalArgumentException.class, () -> new Quantity(-1));
        }
    }

    @Nested
    @DisplayName("Factory methods")
    class FactoryMethods {

        @Test
        @DisplayName("Should create Quantity exposing the provided value")
        void shouldCreateQuantityWithProvidedValue() {
            Quantity quantity = Quantity.of(5);

            assertEquals(5, quantity.value());
        }
    }
}
