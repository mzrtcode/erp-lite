package com.mzrt.erp_lite.domain.order;

import java.util.UUID;

/**
 * Unique identifier for OrderItem entity
 */
public record OrderItemId(UUID value) {

    public OrderItemId {
        if (value == null) {
            throw new IllegalArgumentException("OrderItemId cannot be null");
        }
    }

    public static OrderItemId of(UUID value) {
        return new OrderItemId(value);
    }

    public static OrderItemId generate() {
        return new OrderItemId(UUID.randomUUID());
    }
}
