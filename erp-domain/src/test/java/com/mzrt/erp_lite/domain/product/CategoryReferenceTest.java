package com.mzrt.erp_lite.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CategoryReference DomainTest")
class CategoryReferenceTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidation {

        @Test
        @DisplayName("Should throw IllegalArgumentException when categoryId is null")
        void shouldThrowIllegalArgumentExceptionWhenCategoryIdIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new CategoryReference(null));

            assertEquals("CategoryReference cannot be null or blank", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when categoryId is blank")
        void shouldThrowIllegalArgumentExceptionWhenCategoryIdIsBlank() {
            assertThrows(IllegalArgumentException.class, () -> new CategoryReference("   "));
        }
    }

    @Nested
    @DisplayName("Factory methods")
    class FactoryMethods {

        @Test
        @DisplayName("Should create CategoryReference exposing the provided value")
        void shouldCreateCategoryReferenceWithProvidedValue() {
            CategoryReference reference = CategoryReference.of("cat-electronics");

            assertEquals("cat-electronics", reference.categoryId());
        }
    }
}
