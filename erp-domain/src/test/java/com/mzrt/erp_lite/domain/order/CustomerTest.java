package com.mzrt.erp_lite.domain.order;

import com.mzrt.erp_lite.domain.shared.CustomerId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Customer DomainTest")
class CustomerTest {

    private static final CustomerId VALID_CUSTOMER_ID = CustomerId.of(1L);
    private static final String VALID_NAME = "John Doe";

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidation {

        @Test
        @DisplayName("Should throw IllegalArgumentException when customerId is null")
        void shouldThrowIllegalArgumentExceptionWhenCustomerIdIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Customer(null, VALID_NAME));

            assertEquals("Customer ID cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when customerName is null")
        void shouldThrowIllegalArgumentExceptionWhenCustomerNameIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Customer(VALID_CUSTOMER_ID, null));

            assertEquals("Customer name cannot be null or blank", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when customerName is blank")
        void shouldThrowIllegalArgumentExceptionWhenCustomerNameIsBlank() {
            assertThrows(IllegalArgumentException.class, () -> new Customer(VALID_CUSTOMER_ID, "   "));
        }
    }

    @Nested
    @DisplayName("Factory methods")
    class FactoryMethods {

        @Test
        @DisplayName("Should create Customer exposing the provided fields")
        void shouldCreateCustomerWithProvidedFields() {
            Customer customer = Customer.of(VALID_CUSTOMER_ID, VALID_NAME);

            assertEquals(VALID_CUSTOMER_ID, customer.customerId());
            assertEquals(VALID_NAME, customer.customerName());
        }
    }
}
