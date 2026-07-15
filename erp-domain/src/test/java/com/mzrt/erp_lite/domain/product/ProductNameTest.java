package com.mzrt.erp_lite.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductName DomainTest")
class ProductNameTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidation {

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void shouldThrowIllegalArgumentExceptionWhenValueIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new ProductName(null));

            assertEquals("ProductName cannot be null or blank", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is blank")
        void shouldThrowIllegalArgumentExceptionWhenValueIsBlank() {
            assertThrows(IllegalArgumentException.class, () -> new ProductName("   "));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is shorter than the minimum length")
        void shouldThrowIllegalArgumentExceptionWhenValueIsTooShort() {
            assertThrows(IllegalArgumentException.class, () -> new ProductName("ab"));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is longer than the maximum length")
        void shouldThrowIllegalArgumentExceptionWhenValueIsTooLong() {
            String tooLong = "a".repeat(201);

            assertThrows(IllegalArgumentException.class, () -> new ProductName(tooLong));
        }

        @Test
        @DisplayName("Should accept a value at the minimum length boundary")
        void shouldAcceptValueAtMinimumLengthBoundary() {
            ProductName productName = new ProductName("abc");

            assertEquals("abc", productName.value());
        }

        @Test
        @DisplayName("Should accept a value at the maximum length boundary")
        void shouldAcceptValueAtMaximumLengthBoundary() {
            String maxLength = "a".repeat(200);

            ProductName productName = new ProductName(maxLength);

            assertEquals(maxLength, productName.value());
        }
    }

    @Nested
    @DisplayName("Factory methods")
    class FactoryMethods {

        @Test
        @DisplayName("Should create ProductName exposing the provided value when valid")
        void shouldCreateProductNameWithProvidedValueWhenValid() {
            ProductName productName = ProductName.of("Wireless Mouse");

            assertEquals("Wireless Mouse", productName.value());
        }
    }
}
