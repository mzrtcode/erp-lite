package com.mzrt.erp_lite.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderNumber DomainTest")
class OrderNumberTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidation {

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void shouldThrowIllegalArgumentExceptionWhenValueIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new OrderNumber(null));

            assertEquals("Order number cannot be null", exception.getMessage());
        }

        @ParameterizedTest
        @DisplayName("Should throw IllegalArgumentException when format does not match the pattern")
        @ValueSource(strings = {"ORD-25-001", "ORD-2025-1", "2025-001", "ord-2025-001", "ORD-2025-0001"})
        void shouldThrowIllegalArgumentExceptionWhenFormatIsInvalid(String invalid) {
            assertThrows(IllegalArgumentException.class, () -> new OrderNumber(invalid));
        }
    }

    @Nested
    @DisplayName("Factory methods")
    class FactoryMethods {

        @Test
        @DisplayName("Should create OrderNumber exposing the provided value when valid")
        void shouldCreateOrderNumberWithProvidedValueWhenValid() {
            OrderNumber orderNumber = OrderNumber.of("ORD-2025-001");

            assertEquals("ORD-2025-001", orderNumber.value());
        }

        @Test
        @DisplayName("Should generate an OrderNumber with the current year and given sequence")
        void shouldGenerateOrderNumberWithCurrentYearAndGivenSequence() {
            OrderNumber orderNumber = OrderNumber.generate(7);

            assertEquals("ORD-" + Year.now().getValue() + "-007", orderNumber.value());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when sequence is below the minimum")
        void shouldThrowIllegalArgumentExceptionWhenSequenceIsBelowMinimum() {
            assertThrows(IllegalArgumentException.class, () -> OrderNumber.generate(0));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when sequence is above the maximum")
        void shouldThrowIllegalArgumentExceptionWhenSequenceIsAboveMaximum() {
            assertThrows(IllegalArgumentException.class, () -> OrderNumber.generate(1000));
        }
    }
}
