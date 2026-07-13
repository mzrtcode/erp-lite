package com.mzrt.erp_lite.domain.order.events;

import com.mzrt.erp_lite.domain.common.DomainEvent;
import com.mzrt.erp_lite.domain.order.OrderId;

import java.time.Instant;

/**
 * Emitted when order is canceled.
 * If was CONFIRMED, stock must be released.
 *
 * @param orderId   the order identifier
 * @param reason    the cancellation reason
 * @param timestamp the event timestamp
 */
public record OrderCancelled(
        OrderId orderId,
        String reason,
        Instant timestamp
) implements DomainEvent {
}