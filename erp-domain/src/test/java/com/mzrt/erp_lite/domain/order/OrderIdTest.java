package com.mzrt.erp_lite.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderId DomainTest")
class OrderIdTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidation {

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void shouldThrowIllegalArgumentExceptionWhenValueIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new OrderId(null));

            assertEquals("OrderId cannot be null", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Factory methods")
    class FactoryMethods {

        @Test
        @DisplayName("Should create OrderId exposing the provided value")
        void shouldCreateOrderIdWithProvidedValue() {
            UUID uuid = UUID.randomUUID();

            OrderId orderId = OrderId.of(uuid);

            assertEquals(uuid, orderId.value());
        }

        @Test
        @DisplayName("Should generate a new OrderId with a non-null value")
        void shouldGenerateNewOrderIdWithNonNullValue() {
            OrderId orderId = OrderId.generate();

            assertNotNull(orderId.value());
        }

        @Test
        @DisplayName("Should generate unique OrderIds on each call")
        void shouldGenerateUniqueOrderIdsOnEachCall() {
            OrderId first = OrderId.generate();
            OrderId second = OrderId.generate();

            assertNotEquals(first, second);
        }
    }
}
