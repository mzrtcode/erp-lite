package com.mzrt.erp_lite.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SKU DomainTest")
class SKUTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidation {

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void shouldThrowIllegalArgumentExceptionWhenValueIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new SKU(null));

            assertEquals("SKU cannot be null", exception.getMessage());
        }

        @ParameterizedTest
        @DisplayName("Should throw IllegalArgumentException when format does not match the pattern")
        @ValueSource(strings = {"laptop-001", "LAPTOP001", "LAPTOP-1", "LAPTOP-0001", "-001", "LAPTOP-"})
        void shouldThrowIllegalArgumentExceptionWhenFormatIsInvalid(String invalidSku) {
            assertThrows(IllegalArgumentException.class, () -> new SKU(invalidSku));
        }
    }

    @Nested
    @DisplayName("Factory methods")
    class FactoryMethods {

        @Test
        @DisplayName("Should create SKU exposing the provided value when valid")
        void shouldCreateSkuWithProvidedValueWhenValid() {
            SKU sku = SKU.of("LAPTOP-001");

            assertEquals("LAPTOP-001", sku.value());
        }
    }
}
