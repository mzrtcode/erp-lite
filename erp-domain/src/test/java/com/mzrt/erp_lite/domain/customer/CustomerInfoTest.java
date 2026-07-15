package com.mzrt.erp_lite.domain.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CustomerInfo DomainTest")
class CustomerInfoTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidation {

        @Test
        @DisplayName("Should throw IllegalArgumentException when id is null")
        void shouldThrowIllegalArgumentExceptionWhenIdIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    new CustomerInfo(null, "John Doe", "john@example.com", "123456", "Street 1",
                            "City", "00000", "ACME"));

            assertEquals("Id is null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when name is null")
        void shouldThrowIllegalArgumentExceptionWhenNameIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    new CustomerInfo(1L, null, "john@example.com", "123456", "Street 1",
                            "City", "00000", "ACME"));

            assertEquals("Name is not present", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when name is blank")
        void shouldThrowIllegalArgumentExceptionWhenNameIsBlank() {
            assertThrows(IllegalArgumentException.class, () ->
                    new CustomerInfo(1L, "   ", "john@example.com", "123456", "Street 1",
                            "City", "00000", "ACME"));
        }

        @Test
        @DisplayName("Should create CustomerInfo exposing all provided fields")
        void shouldCreateCustomerInfoWithAllProvidedFields() {
            CustomerInfo customerInfo = new CustomerInfo(1L, "John Doe", "john@example.com", "123456",
                    "Street 1", "City", "00000", "ACME");

            assertEquals(1L, customerInfo.id());
            assertEquals("John Doe", customerInfo.name());
            assertEquals("john@example.com", customerInfo.email());
            assertEquals("123456", customerInfo.phone());
            assertEquals("Street 1", customerInfo.address());
            assertEquals("City", customerInfo.city());
            assertEquals("00000", customerInfo.zipcode());
            assertEquals("ACME", customerInfo.companyName());
        }
    }
}
