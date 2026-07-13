package com.mzrt.erp_lite.domain.product;

/**
 * Product name
 */
public record ProductName(String value) {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 200;

    public ProductName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ProductName cannot be null or blank");
        }
        if (value.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("ProductName must have at least " + MIN_LENGTH + " characters");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("ProductName must have at most " + MAX_LENGTH + " characters");
        }
    }

    public static ProductName of(String value) {
        return new ProductName(value);
    }
}
