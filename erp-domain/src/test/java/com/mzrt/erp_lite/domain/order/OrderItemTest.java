package com.mzrt.erp_lite.domain.order;

import com.mzrt.erp_lite.domain.product.CategoryReference;
import com.mzrt.erp_lite.domain.product.Product;
import com.mzrt.erp_lite.domain.product.ProductName;
import com.mzrt.erp_lite.domain.product.SKU;
import com.mzrt.erp_lite.domain.product.Stock;
import com.mzrt.erp_lite.domain.shared.Money;
import com.mzrt.erp_lite.domain.shared.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderItem DomainTest")
class OrderItemTest {

    private static final Currency USD = Currency.getInstance("USD");

    private static Product newProduct(Money price, Stock stock) {
        return Product.create(
                SKU.of("LAPTOP-001"),
                ProductName.of("Gaming Laptop"),
                "A powerful gaming laptop",
                price,
                stock,
                CategoryReference.of("cat-electronics"),
                null,
                "system"
        );
    }

    private static Product newActiveProduct() {
        return newProduct(Money.of(BigDecimal.valueOf(100), USD), Stock.of(10));
    }

    @Nested
    @DisplayName("from")
    class From {

        @Test
        @DisplayName("Should throw IllegalArgumentException when product is null")
        void shouldThrowIllegalArgumentExceptionWhenProductIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> OrderItem.from(null, Quantity.of(1)));

            assertEquals("Product cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when quantity is null")
        void shouldThrowIllegalArgumentExceptionWhenQuantityIsNull() {
            Product product = newActiveProduct();

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> OrderItem.from(product, null));

            assertEquals("Quantity cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when product is inactive")
        void shouldThrowIllegalArgumentExceptionWhenProductIsInactive() {
            Product product = newActiveProduct();
            product.deactivate();

            assertThrows(IllegalArgumentException.class, () -> OrderItem.from(product, Quantity.of(1)));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when stock is insufficient")
        void shouldThrowIllegalArgumentExceptionWhenStockIsInsufficient() {
            Product product = newProduct(Money.of(BigDecimal.valueOf(100), USD), Stock.of(2));

            assertThrows(IllegalArgumentException.class, () -> OrderItem.from(product, Quantity.of(5)));
        }

        @Test
        @DisplayName("Should create an OrderItem snapshotting the product name and price")
        void shouldCreateOrderItemSnapshottingProductNameAndPrice() {
            Product product = newActiveProduct();

            OrderItem orderItem = OrderItem.from(product, Quantity.of(3));

            assertEquals(product.getId(), orderItem.getProductReference());
            assertEquals(product.getName().value(), orderItem.getProductName());
            assertEquals(product.getPrice(), orderItem.getUnitPrice());
            assertEquals(Quantity.of(3), orderItem.getQuantity());
        }

        @Test
        @DisplayName("Should compute the subtotal as quantity times unit price")
        void shouldComputeSubtotalAsQuantityTimesUnitPrice() {
            Product product = newActiveProduct();

            OrderItem orderItem = OrderItem.from(product, Quantity.of(3));

            assertEquals(Money.of(BigDecimal.valueOf(300), USD), orderItem.getSubtotal());
        }
    }

    @Nested
    @DisplayName("calculateSubtotal")
    class CalculateSubtotal {

        @Test
        @DisplayName("Should return the same value as unitPrice multiplied by quantity")
        void shouldReturnSameValueAsUnitPriceMultipliedByQuantity() {
            Product product = newActiveProduct();
            OrderItem orderItem = OrderItem.from(product, Quantity.of(4));

            Money calculated = orderItem.calculateSubtotal();

            assertEquals(orderItem.getSubtotal(), calculated);
        }
    }
}
