package com.mzrt.erp_lite.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderStatus DomainTest")
class OrderStatusTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidation {

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void shouldThrowIllegalArgumentExceptionWhenValueIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new OrderStatus(null));

            assertEquals("Order status cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is not a valid status")
        void shouldThrowIllegalArgumentExceptionWhenValueIsNotValidStatus() {
            assertThrows(IllegalArgumentException.class, () -> new OrderStatus("UNKNOWN"));
        }
    }

    @Nested
    @DisplayName("Factory methods")
    class FactoryMethods {

        @Test
        @DisplayName("Should create PENDING status")
        void shouldCreatePendingStatus() {
            assertEquals(OrderStatus.PENDING, OrderStatus.pending().value());
        }

        @Test
        @DisplayName("Should create CONFIRMED status")
        void shouldCreateConfirmedStatus() {
            assertEquals(OrderStatus.CONFIRMED, OrderStatus.confirmed().value());
        }

        @Test
        @DisplayName("Should create SHIPPED status")
        void shouldCreateShippedStatus() {
            assertEquals(OrderStatus.SHIPPED, OrderStatus.shipped().value());
        }

        @Test
        @DisplayName("Should create DELIVERED status")
        void shouldCreateDeliveredStatus() {
            assertEquals(OrderStatus.DELIVERED, OrderStatus.delivered().value());
        }

        @Test
        @DisplayName("Should create CANCELLED status")
        void shouldCreateCancelledStatus() {
            assertEquals(OrderStatus.CANCELLED, OrderStatus.cancelled().value());
        }
    }

    @Nested
    @DisplayName("canTransitionTo")
    class CanTransitionTo {

        @Test
        @DisplayName("Should allow PENDING to CONFIRMED")
        void shouldAllowPendingToConfirmed() {
            assertTrue(OrderStatus.pending().canTransitionTo(OrderStatus.confirmed()));
        }

        @Test
        @DisplayName("Should allow PENDING to CANCELLED")
        void shouldAllowPendingToCancelled() {
            assertTrue(OrderStatus.pending().canTransitionTo(OrderStatus.cancelled()));
        }

        @Test
        @DisplayName("Should not allow PENDING to SHIPPED")
        void shouldNotAllowPendingToShipped() {
            assertFalse(OrderStatus.pending().canTransitionTo(OrderStatus.shipped()));
        }

        @Test
        @DisplayName("Should allow CONFIRMED to SHIPPED")
        void shouldAllowConfirmedToShipped() {
            assertTrue(OrderStatus.confirmed().canTransitionTo(OrderStatus.shipped()));
        }

        @Test
        @DisplayName("Should allow CONFIRMED to CANCELLED")
        void shouldAllowConfirmedToCancelled() {
            assertTrue(OrderStatus.confirmed().canTransitionTo(OrderStatus.cancelled()));
        }

        @Test
        @DisplayName("Should not allow CONFIRMED to DELIVERED")
        void shouldNotAllowConfirmedToDelivered() {
            assertFalse(OrderStatus.confirmed().canTransitionTo(OrderStatus.delivered()));
        }

        @Test
        @DisplayName("Should allow SHIPPED to DELIVERED")
        void shouldAllowShippedToDelivered() {
            assertTrue(OrderStatus.shipped().canTransitionTo(OrderStatus.delivered()));
        }

        @Test
        @DisplayName("Should not allow SHIPPED to CANCELLED")
        void shouldNotAllowShippedToCancelled() {
            assertFalse(OrderStatus.shipped().canTransitionTo(OrderStatus.cancelled()));
        }

        @Test
        @DisplayName("Should not allow any transition from DELIVERED")
        void shouldNotAllowAnyTransitionFromDelivered() {
            assertFalse(OrderStatus.delivered().canTransitionTo(OrderStatus.pending()));
            assertFalse(OrderStatus.delivered().canTransitionTo(OrderStatus.cancelled()));
        }

        @Test
        @DisplayName("Should not allow any transition from CANCELLED")
        void shouldNotAllowAnyTransitionFromCancelled() {
            assertFalse(OrderStatus.cancelled().canTransitionTo(OrderStatus.pending()));
            assertFalse(OrderStatus.cancelled().canTransitionTo(OrderStatus.confirmed()));
        }
    }

    @Nested
    @DisplayName("Status predicates")
    class StatusPredicates {

        @Test
        @DisplayName("Should identify PENDING status")
        void shouldIdentifyPendingStatus() {
            assertTrue(OrderStatus.pending().isPending());
            assertFalse(OrderStatus.pending().isFinalState());
        }

        @Test
        @DisplayName("Should identify CONFIRMED status")
        void shouldIdentifyConfirmedStatus() {
            assertTrue(OrderStatus.confirmed().isConfirmed());
        }

        @Test
        @DisplayName("Should identify SHIPPED status")
        void shouldIdentifyShippedStatus() {
            assertTrue(OrderStatus.shipped().isShipped());
        }

        @Test
        @DisplayName("Should identify DELIVERED as a final state")
        void shouldIdentifyDeliveredAsFinalState() {
            assertTrue(OrderStatus.delivered().isDelivered());
            assertTrue(OrderStatus.delivered().isFinalState());
        }

        @Test
        @DisplayName("Should identify CANCELLED as a final state")
        void shouldIdentifyCancelledAsFinalState() {
            assertTrue(OrderStatus.cancelled().isCancelled());
            assertTrue(OrderStatus.cancelled().isFinalState());
        }
    }
}
