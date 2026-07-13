package com.mzrt.erp_lite.domain.product;

import java.util.UUID;

/**
 * Unique identifier for Product aggregate.
 *
 * @param value the UUID value
 */
public record ProductId(UUID value) {

    public ProductId {
        if (value == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
    }

    public static ProductId of(UUID value) {
        return new ProductId(value);
    }

    public static ProductId generate() {
        return new ProductId(UUID.randomUUID());
    }
}