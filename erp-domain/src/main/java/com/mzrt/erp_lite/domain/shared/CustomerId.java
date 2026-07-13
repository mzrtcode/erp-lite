package com.mzrt.erp_lite.domain.shared;

/**
 * Reference to external customer system (JSONPlaceholder)
 */
public record CustomerId(Long value) {

    public CustomerId {
        if (value == null) {
            throw new IllegalArgumentException("CustomerId cannot be null");
        }
        if (value <= 0) {
            throw new IllegalArgumentException("CustomerId must be greater than 0");
        }
    }

    public static CustomerId of(Long value) {
        return new CustomerId(value);
    }
}
