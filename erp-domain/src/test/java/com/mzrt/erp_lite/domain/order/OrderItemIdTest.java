package com.mzrt.erp_lite.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderItemId DomainTest")
class OrderItemIdTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidation {

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void shouldThrowIllegalArgumentExceptionWhenValueIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new OrderItemId(null));

            assertEquals("OrderItemId cannot be null", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Factory methods")
    class FactoryMethods {

        @Test
        @DisplayName("Should create OrderItemId exposing the provided value")
        void shouldCreateOrderItemIdWithProvidedValue() {
            UUID uuid = UUID.randomUUID();

            OrderItemId orderItemId = OrderItemId.of(uuid);

            assertEquals(uuid, orderItemId.value());
        }

        @Test
        @DisplayName("Should generate a new OrderItemId with a non-null value")
        void shouldGenerateNewOrderItemIdWithNonNullValue() {
            OrderItemId orderItemId = OrderItemId.generate();

            assertNotNull(orderItemId.value());
        }

        @Test
        @DisplayName("Should generate unique OrderItemIds on each call")
        void shouldGenerateUniqueOrderItemIdsOnEachCall() {
            OrderItemId first = OrderItemId.generate();
            OrderItemId second = OrderItemId.generate();

            assertNotEquals(first, second);
        }
    }
}
