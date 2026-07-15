package com.mzrt.erp_lite.domain.order;

import com.mzrt.erp_lite.domain.order.events.OrderCancelled;
import com.mzrt.erp_lite.domain.order.events.OrderConfirmed;
import com.mzrt.erp_lite.domain.order.events.OrderCreated;
import com.mzrt.erp_lite.domain.order.events.OrderDelivered;
import com.mzrt.erp_lite.domain.order.events.OrderShipped;
import com.mzrt.erp_lite.domain.product.CategoryReference;
import com.mzrt.erp_lite.domain.product.Product;
import com.mzrt.erp_lite.domain.product.ProductName;
import com.mzrt.erp_lite.domain.product.SKU;
import com.mzrt.erp_lite.domain.product.Stock;
import com.mzrt.erp_lite.domain.shared.CustomerId;
import com.mzrt.erp_lite.domain.shared.Money;
import com.mzrt.erp_lite.domain.shared.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Order DomainTest")
class OrderTest {

    private static final Currency USD = Currency.getInstance("USD");
    private static final OrderNumber VALID_ORDER_NUMBER = OrderNumber.of("ORD-2025-001");
    private static final Customer VALID_CUSTOMER = Customer.of(CustomerId.of(1L), "John Doe");
    private static final String VALID_CREATED_BY = "system";

    private static Product newProduct(String sku, BigDecimal price, int stock) {
        return Product.create(
                SKU.of(sku),
                ProductName.of("Some product"),
                "Description",
                Money.of(price, USD),
                Stock.of(stock),
                CategoryReference.of("cat-electronics"),
                null,
                VALID_CREATED_BY
        );
    }

    private static OrderItem newOrderItem(BigDecimal price, int quantity) {
        Product product = newProduct("PROD-001", price, quantity + 10);
        return OrderItem.from(product, Quantity.of(quantity));
    }

    private static Order newPendingOrder() {
        OrderItem item = newOrderItem(BigDecimal.valueOf(100), 2);
        return Order.create(VALID_ORDER_NUMBER, VALID_CUSTOMER, List.of(item), VALID_CREATED_BY);
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("Should throw IllegalArgumentException when order number is null")
        void shouldThrowIllegalArgumentExceptionWhenOrderNumberIsNull() {
            OrderItem item = newOrderItem(BigDecimal.valueOf(100), 1);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    Order.create(null, VALID_CUSTOMER, List.of(item), VALID_CREATED_BY));

            assertEquals("Order number cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when customer is null")
        void shouldThrowIllegalArgumentExceptionWhenCustomerIsNull() {
            OrderItem item = newOrderItem(BigDecimal.valueOf(100), 1);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    Order.create(VALID_ORDER_NUMBER, null, List.of(item), VALID_CREATED_BY));

            assertEquals("Customer cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when items is null")
        void shouldThrowIllegalArgumentExceptionWhenItemsIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    Order.create(VALID_ORDER_NUMBER, VALID_CUSTOMER, null, VALID_CREATED_BY));

            assertEquals("Order must have at least one item", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when items is empty")
        void shouldThrowIllegalArgumentExceptionWhenItemsIsEmpty() {
            assertThrows(IllegalArgumentException.class, () ->
                    Order.create(VALID_ORDER_NUMBER, VALID_CUSTOMER, List.of(), VALID_CREATED_BY));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when items have different currencies")
        void shouldThrowIllegalArgumentExceptionWhenItemsHaveDifferentCurrencies() {
            Product usdProduct = newProduct("PROD-001", BigDecimal.valueOf(100), 10);
            Product eurProduct = Product.create(
                    SKU.of("PROD-002"),
                    ProductName.of("Other product"),
                    "Description",
                    Money.of(BigDecimal.valueOf(50), Currency.getInstance("EUR")),
                    Stock.of(10),
                    CategoryReference.of("cat-electronics"),
                    null,
                    VALID_CREATED_BY
            );
            OrderItem usdItem = OrderItem.from(usdProduct, Quantity.of(1));
            OrderItem eurItem = OrderItem.from(eurProduct, Quantity.of(1));

            assertThrows(IllegalArgumentException.class, () ->
                    Order.create(VALID_ORDER_NUMBER, VALID_CUSTOMER, List.of(usdItem, eurItem), VALID_CREATED_BY));
        }

        @Test
        @DisplayName("Should create an Order in PENDING status")
        void shouldCreateOrderInPendingStatus() {
            Order order = newPendingOrder();

            assertTrue(order.getStatus().isPending());
        }

        @Test
        @DisplayName("Should calculate the total amount from item subtotals")
        void shouldCalculateTotalAmountFromItemSubtotals() {
            Order order = newPendingOrder();

            assertEquals(Money.of(BigDecimal.valueOf(200), USD), order.getTotalAmount());
        }

        @Test
        @DisplayName("Should register an OrderCreated event")
        void shouldRegisterOrderCreatedEvent() {
            Order order = newPendingOrder();

            assertEquals(1, order.getDomainEvents().size());
            assertInstanceOf(OrderCreated.class, order.getDomainEvents().get(0));
        }
    }

    @Nested
    @DisplayName("confirm")
    class Confirm {

        @Test
        @DisplayName("Should transition from PENDING to CONFIRMED")
        void shouldTransitionFromPendingToConfirmed() {
            Order order = newPendingOrder();

            order.confirm();

            assertTrue(order.getStatus().isConfirmed());
        }

        @Test
        @DisplayName("Should register an OrderConfirmed event")
        void shouldRegisterOrderConfirmedEvent() {
            Order order = newPendingOrder();
            order.clearDomainEvents();

            order.confirm();

            assertInstanceOf(OrderConfirmed.class, order.getDomainEvents().get(0));
        }

        @Test
        @DisplayName("Should throw IllegalStateException when order is not PENDING")
        void shouldThrowIllegalStateExceptionWhenOrderIsNotPending() {
            Order order = newPendingOrder();
            order.confirm();

            assertThrows(IllegalStateException.class, order::confirm);
        }
    }

    @Nested
    @DisplayName("ship")
    class Ship {

        @Test
        @DisplayName("Should transition from CONFIRMED to SHIPPED")
        void shouldTransitionFromConfirmedToShipped() {
            Order order = newPendingOrder();
            order.confirm();

            order.ship();

            assertTrue(order.getStatus().isShipped());
        }

        @Test
        @DisplayName("Should register an OrderShipped event")
        void shouldRegisterOrderShippedEvent() {
            Order order = newPendingOrder();
            order.confirm();
            order.clearDomainEvents();

            order.ship();

            assertInstanceOf(OrderShipped.class, order.getDomainEvents().get(0));
        }

        @Test
        @DisplayName("Should throw IllegalStateException when order is still PENDING")
        void shouldThrowIllegalStateExceptionWhenOrderIsStillPending() {
            Order order = newPendingOrder();

            assertThrows(IllegalStateException.class, order::ship);
        }
    }

    @Nested
    @DisplayName("deliver")
    class Deliver {

        @Test
        @DisplayName("Should transition from SHIPPED to DELIVERED")
        void shouldTransitionFromShippedToDelivered() {
            Order order = newPendingOrder();
            order.confirm();
            order.ship();

            order.deliver();

            assertTrue(order.getStatus().isDelivered());
        }

        @Test
        @DisplayName("Should register an OrderDelivered event")
        void shouldRegisterOrderDeliveredEvent() {
            Order order = newPendingOrder();
            order.confirm();
            order.ship();
            order.clearDomainEvents();

            order.deliver();

            assertInstanceOf(OrderDelivered.class, order.getDomainEvents().get(0));
        }

        @Test
        @DisplayName("Should throw IllegalStateException when order is not SHIPPED")
        void shouldThrowIllegalStateExceptionWhenOrderIsNotShipped() {
            Order order = newPendingOrder();
            order.confirm();

            assertThrows(IllegalStateException.class, order::deliver);
        }
    }

    @Nested
    @DisplayName("cancel")
    class Cancel {

        @Test
        @DisplayName("Should throw IllegalArgumentException when reason is null")
        void shouldThrowIllegalArgumentExceptionWhenReasonIsNull() {
            Order order = newPendingOrder();

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> order.cancel(null));

            assertEquals("Cancellation reason cannot be null or blank", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when reason is blank")
        void shouldThrowIllegalArgumentExceptionWhenReasonIsBlank() {
            Order order = newPendingOrder();

            assertThrows(IllegalArgumentException.class, () -> order.cancel("   "));
        }

        @Test
        @DisplayName("Should cancel a PENDING order")
        void shouldCancelPendingOrder() {
            Order order = newPendingOrder();

            order.cancel("Customer changed mind");

            assertTrue(order.getStatus().isCancelled());
        }

        @Test
        @DisplayName("Should cancel a CONFIRMED order")
        void shouldCancelConfirmedOrder() {
            Order order = newPendingOrder();
            order.confirm();

            order.cancel("Out of stock");

            assertTrue(order.getStatus().isCancelled());
        }

        @Test
        @DisplayName("Should register an OrderCancelled event with the given reason")
        void shouldRegisterOrderCancelledEventWithGivenReason() {
            Order order = newPendingOrder();
            order.clearDomainEvents();

            order.cancel("Customer changed mind");

            OrderCancelled event = (OrderCancelled) order.getDomainEvents().get(0);
            assertEquals("Customer changed mind", event.reason());
        }

        @Test
        @DisplayName("Should throw IllegalStateException when order is already DELIVERED")
        void shouldThrowIllegalStateExceptionWhenOrderIsAlreadyDelivered() {
            Order order = newPendingOrder();
            order.confirm();
            order.ship();
            order.deliver();

            assertThrows(IllegalStateException.class, () -> order.cancel("Too late"));
        }
    }

    @Nested
    @DisplayName("addItem")
    class AddItem {

        @Test
        @DisplayName("Should add an item and recalculate the total amount")
        void shouldAddItemAndRecalculateTotalAmount() {
            Order order = newPendingOrder();
            OrderItem newItem = newOrderItem(BigDecimal.valueOf(50), 1);

            order.addItem(newItem);

            assertEquals(2, order.getItems().size());
            assertEquals(Money.of(BigDecimal.valueOf(250), USD), order.getTotalAmount());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when item is null")
        void shouldThrowIllegalArgumentExceptionWhenItemIsNull() {
            Order order = newPendingOrder();

            assertThrows(IllegalArgumentException.class, () -> order.addItem(null));
        }

        @Test
        @DisplayName("Should throw IllegalStateException when order is not PENDING")
        void shouldThrowIllegalStateExceptionWhenOrderIsNotPending() {
            Order order = newPendingOrder();
            order.confirm();
            OrderItem newItem = newOrderItem(BigDecimal.valueOf(50), 1);

            assertThrows(IllegalStateException.class, () -> order.addItem(newItem));
        }
    }

    @Nested
    @DisplayName("removeItem")
    class RemoveItem {

        @Test
        @DisplayName("Should remove an item and recalculate the total amount")
        void shouldRemoveItemAndRecalculateTotalAmount() {
            Order order = newPendingOrder();
            OrderItem extraItem = newOrderItem(BigDecimal.valueOf(50), 1);
            order.addItem(extraItem);

            order.removeItem(extraItem);

            assertEquals(1, order.getItems().size());
            assertEquals(Money.of(BigDecimal.valueOf(200), USD), order.getTotalAmount());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when item is null")
        void shouldThrowIllegalArgumentExceptionWhenItemIsNull() {
            Order order = newPendingOrder();

            assertThrows(IllegalArgumentException.class, () -> order.removeItem(null));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when item is not part of the order")
        void shouldThrowIllegalArgumentExceptionWhenItemIsNotInOrder() {
            Order order = newPendingOrder();
            OrderItem foreignItem = newOrderItem(BigDecimal.valueOf(50), 1);

            assertThrows(IllegalArgumentException.class, () -> order.removeItem(foreignItem));
        }

        @Test
        @DisplayName("Should throw IllegalStateException when removing the last remaining item")
        void shouldThrowIllegalStateExceptionWhenRemovingLastRemainingItem() {
            Order order = newPendingOrder();
            OrderItem onlyItem = order.getItems().get(0);

            assertThrows(IllegalStateException.class, () -> order.removeItem(onlyItem));
        }

        @Test
        @DisplayName("Should throw IllegalStateException when order is not PENDING")
        void shouldThrowIllegalStateExceptionWhenOrderIsNotPending() {
            Order order = newPendingOrder();
            OrderItem extraItem = newOrderItem(BigDecimal.valueOf(50), 1);
            order.addItem(extraItem);
            order.confirm();

            assertThrows(IllegalStateException.class, () -> order.removeItem(extraItem));
        }
    }

    @Nested
    @DisplayName("getItems")
    class GetItems {

        @Test
        @DisplayName("Should return an unmodifiable list of items")
        void shouldReturnUnmodifiableListOfItems() {
            Order order = newPendingOrder();

            assertThrows(UnsupportedOperationException.class,
                    () -> order.getItems().add(newOrderItem(BigDecimal.valueOf(50), 1)));
        }
    }
}
