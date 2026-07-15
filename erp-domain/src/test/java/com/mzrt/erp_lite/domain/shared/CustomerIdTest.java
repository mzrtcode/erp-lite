package com.mzrt.erp_lite.domain.shared;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CustomerId DomainTest")
class CustomerIdTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidation {

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void shouldThrowIllegalArgumentExceptionWhenValueIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new CustomerId(null));

            assertEquals("CustomerId cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is zero")
        void shouldThrowIllegalArgumentExceptionWhenValueIsZero() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new CustomerId(0L));

            assertEquals("CustomerId must be greater than 0", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is negative")
        void shouldThrowIllegalArgumentExceptionWhenValueIsNegative() {
            assertThrows(IllegalArgumentException.class, () -> new CustomerId(-1L));
        }
    }

    @Nested
    @DisplayName("Factory methods")
    class FactoryMethods {

        @Test
        @DisplayName("Should create CustomerId exposing the provided value")
        void shouldCreateCustomerIdWithProvidedValue() {
            CustomerId customerId = CustomerId.of(42L);

            assertEquals(42L, customerId.value());
        }
    }
}
