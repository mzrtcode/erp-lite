package com.mzrt.erp_lite.domain.catalog;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CatalogType DomainTest")
class CatalogTypeTest {

    @Nested
    @DisplayName("fromCode")
    class FromCode {

        @Test
        @DisplayName("Should throw IllegalArgumentException when code is null")
        void shouldThrowIllegalArgumentExceptionWhenCodeIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> CatalogType.fromCode(null));

            assertEquals("Catalog type code cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when code is blank")
        void shouldThrowIllegalArgumentExceptionWhenCodeIsBlank() {
            assertThrows(IllegalArgumentException.class, () -> CatalogType.fromCode("   "));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when code is unknown")
        void shouldThrowIllegalArgumentExceptionWhenCodeIsUnknown() {
            assertThrows(IllegalArgumentException.class, () -> CatalogType.fromCode("UNKNOWN_CODE"));
        }

        @Test
        @DisplayName("Should return the matching enum for a valid code")
        void shouldReturnMatchingEnumForValidCode() {
            assertEquals(CatalogType.PRODUCT_CATEGORIES, CatalogType.fromCode("PRODUCT_CATEGORIES"));
            assertEquals(CatalogType.CURRENCIES, CatalogType.fromCode("CURRENCIES"));
        }
    }

    @Nested
    @DisplayName("isValid")
    class IsValid {

        @Test
        @DisplayName("Should return true for a valid code")
        void shouldReturnTrueForValidCode() {
            assertTrue(CatalogType.isValid("COUNTRIES"));
        }

        @Test
        @DisplayName("Should return false for an invalid code")
        void shouldReturnFalseForInvalidCode() {
            assertFalse(CatalogType.isValid("UNKNOWN_CODE"));
        }

        @Test
        @DisplayName("Should return false for a null code")
        void shouldReturnFalseForNullCode() {
            assertFalse(CatalogType.isValid(null));
        }
    }
}
