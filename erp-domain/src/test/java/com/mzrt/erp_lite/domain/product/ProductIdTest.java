package com.mzrt.erp_lite.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductId DomainTest")
class ProductIdTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidation {

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void shouldThrowIllegalArgumentExceptionWhenValueIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new ProductId(null));

            assertEquals("Product ID cannot be null", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Factory methods")
    class FactoryMethods {

        @Test
        @DisplayName("Should create ProductId exposing the provided value")
        void shouldCreateProductIdWithProvidedValue() {
            UUID uuid = UUID.randomUUID();

            ProductId productId = ProductId.of(uuid);

            assertEquals(uuid, productId.value());
        }

        @Test
        @DisplayName("Should generate a new ProductId with a non-null value")
        void shouldGenerateNewProductIdWithNonNullValue() {
            ProductId productId = ProductId.generate();

            assertNotNull(productId.value());
        }

        @Test
        @DisplayName("Should generate unique ProductIds on each call")
        void shouldGenerateUniqueProductIdsOnEachCall() {
            ProductId first = ProductId.generate();
            ProductId second = ProductId.generate();

            assertNotEquals(first, second);
        }
    }
}
