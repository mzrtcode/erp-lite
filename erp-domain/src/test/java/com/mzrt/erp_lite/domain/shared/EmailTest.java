package com.mzrt.erp_lite.domain.shared;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Email DomainTest")
class EmailTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidation {

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void shouldThrowIllegalArgumentExceptionWhenValueIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Email(null));

            assertEquals("Email cannot be null or blank", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is blank")
        void shouldThrowIllegalArgumentExceptionWhenValueIsBlank() {
            assertThrows(IllegalArgumentException.class, () -> new Email("   "));
        }

        @ParameterizedTest
        @DisplayName("Should throw IllegalArgumentException when value has an invalid format")
        @ValueSource(strings = {"not-an-email", "missing-domain@", "@missing-local.com", "no-at-sign.com"})
        void shouldThrowIllegalArgumentExceptionWhenFormatIsInvalid(String invalidEmail) {
            assertThrows(IllegalArgumentException.class, () -> new Email(invalidEmail));
        }
    }

    @Nested
    @DisplayName("Factory methods")
    class FactoryMethods {

        @Test
        @DisplayName("Should create Email exposing the provided value when valid")
        void shouldCreateEmailWithProvidedValueWhenValid() {
            Email email = Email.of("john.doe@example.com");

            assertEquals("john.doe@example.com", email.value());
        }
    }
}
