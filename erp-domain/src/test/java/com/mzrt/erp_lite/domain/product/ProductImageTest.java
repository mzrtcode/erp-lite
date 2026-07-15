package com.mzrt.erp_lite.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductImage DomainTest")
class ProductImageTest {

    private static final String VALID_URL = "https://bucket.s3.amazonaws.com/products/laptop.png";

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidation {

        @Test
        @DisplayName("Should throw IllegalArgumentException when imageUrl is null")
        void shouldThrowIllegalArgumentExceptionWhenImageUrlIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new ProductImage(null));

            assertEquals("Image URL cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when URL is not absolute")
        void shouldThrowIllegalArgumentExceptionWhenUrlIsNotAbsolute() {
            assertThrows(IllegalArgumentException.class, () -> new ProductImage("/products/laptop.png"));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when scheme is not http or https")
        void shouldThrowIllegalArgumentExceptionWhenSchemeIsNotHttpOrHttps() {
            assertThrows(IllegalArgumentException.class,
                    () -> new ProductImage("ftp://bucket.s3.amazonaws.com/laptop.png"));
        }
    }

    @Nested
    @DisplayName("Factory methods")
    class FactoryMethods {

        @Test
        @DisplayName("Should create ProductImage exposing the provided value when valid")
        void shouldCreateProductImageWithProvidedValueWhenValid() {
            ProductImage image = ProductImage.of(VALID_URL);

            assertEquals(VALID_URL, image.imageUrl());
        }
    }

    @Nested
    @DisplayName("getFullUrl")
    class GetFullUrl {

        @Test
        @DisplayName("Should return the raw image URL")
        void shouldReturnRawImageUrl() {
            ProductImage image = ProductImage.of(VALID_URL);

            assertEquals(VALID_URL, image.getFullUrl());
        }
    }

    @Nested
    @DisplayName("getFileName")
    class GetFileName {

        @Test
        @DisplayName("Should extract the file name after the last slash")
        void shouldExtractFileNameAfterLastSlash() {
            ProductImage image = ProductImage.of(VALID_URL);

            assertEquals("laptop.png", image.getFileName());
        }

        @Test
        @DisplayName("Should return the full URL when it ends with a trailing slash")
        void shouldReturnFullUrlWhenEndsWithTrailingSlash() {
            ProductImage image = ProductImage.of("https://bucket.s3.amazonaws.com/products/");

            assertEquals("https://bucket.s3.amazonaws.com/products/", image.getFileName());
        }
    }
}
