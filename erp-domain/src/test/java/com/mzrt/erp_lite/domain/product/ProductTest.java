package com.mzrt.erp_lite.domain.product;

import com.mzrt.erp_lite.domain.product.events.ProductCreated;
import com.mzrt.erp_lite.domain.product.events.ProductDeactivated;
import com.mzrt.erp_lite.domain.product.events.ProductUpdated;
import com.mzrt.erp_lite.domain.product.events.StockChanged;
import com.mzrt.erp_lite.domain.shared.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Product DomainTest")
class ProductTest {

    private static final Currency USD = Currency.getInstance("USD");
    private static final SKU VALID_SKU = SKU.of("LAPTOP-001");
    private static final ProductName VALID_NAME = ProductName.of("Gaming Laptop");
    private static final String VALID_DESCRIPTION = "A powerful gaming laptop";
    private static final Money VALID_PRICE = Money.of(BigDecimal.valueOf(1500), USD);
    private static final Stock VALID_STOCK = Stock.of(10);
    private static final CategoryReference VALID_CATEGORY = CategoryReference.of("cat-electronics");
    private static final ProductImage VALID_IMAGE = ProductImage.of("https://bucket.s3.amazonaws.com/laptop.png");
    private static final String VALID_CREATED_BY = "system";

    private static Product newProduct() {
        return Product.create(
                VALID_SKU,
                VALID_NAME,
                VALID_DESCRIPTION,
                VALID_PRICE,
                VALID_STOCK,
                VALID_CATEGORY,
                VALID_IMAGE,
                VALID_CREATED_BY
        );
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("Should throw IllegalArgumentException when price is null")
        void shouldThrowIllegalArgumentExceptionWhenPriceIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    Product.create(VALID_SKU, VALID_NAME, VALID_DESCRIPTION, null, VALID_STOCK,
                            VALID_CATEGORY, VALID_IMAGE, VALID_CREATED_BY));

            assertEquals("Price cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when price is zero")
        void shouldThrowIllegalArgumentExceptionWhenPriceIsZero() {
            Money zeroPrice = Money.of(BigDecimal.ZERO, USD);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    Product.create(VALID_SKU, VALID_NAME, VALID_DESCRIPTION, zeroPrice, VALID_STOCK,
                            VALID_CATEGORY, VALID_IMAGE, VALID_CREATED_BY));

            assertEquals("Price must be greater than 0", exception.getMessage());
        }

        @Test
        @DisplayName("Should create a Product exposing all provided fields")
        void shouldCreateProductWithAllProvidedFields() {
            Product product = newProduct();

            assertNotNull(product.getId());
            assertEquals(VALID_SKU, product.getSku());
            assertEquals(VALID_NAME, product.getName());
            assertEquals(VALID_DESCRIPTION, product.getDescription());
            assertEquals(VALID_PRICE, product.getPrice());
            assertEquals(VALID_STOCK, product.getStock());
            assertEquals(VALID_CATEGORY, product.getCategory());
            assertEquals(VALID_IMAGE, product.getImage());
        }

        @Test
        @DisplayName("Should default active to true when created")
        void shouldDefaultActiveToTrueWhenCreated() {
            Product product = newProduct();

            assertTrue(product.isActive());
        }

        @Test
        @DisplayName("Should register a ProductCreated event")
        void shouldRegisterProductCreatedEvent() {
            Product product = newProduct();

            assertEquals(1, product.getDomainEvents().size());
            assertInstanceOf(ProductCreated.class, product.getDomainEvents().get(0));
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("Should update name, description, price, category and image")
        void shouldUpdateNameDescriptionPriceCategoryAndImage() {
            Product product = newProduct();
            ProductName newName = ProductName.of("Updated Laptop");
            Money newPrice = Money.of(BigDecimal.valueOf(1800), USD);
            CategoryReference newCategory = CategoryReference.of("cat-premium");

            product.update(newName, "Updated description", newPrice, newCategory, VALID_IMAGE);

            assertEquals(newName, product.getName());
            assertEquals("Updated description", product.getDescription());
            assertEquals(newPrice, product.getPrice());
            assertEquals(newCategory, product.getCategory());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when new price is invalid")
        void shouldThrowIllegalArgumentExceptionWhenNewPriceIsInvalid() {
            Product product = newProduct();
            Money invalidPrice = Money.of(BigDecimal.ZERO, USD);

            assertThrows(IllegalArgumentException.class, () ->
                    product.update(VALID_NAME, VALID_DESCRIPTION, invalidPrice, VALID_CATEGORY, VALID_IMAGE));
        }

        @Test
        @DisplayName("Should register a ProductUpdated event")
        void shouldRegisterProductUpdatedEvent() {
            Product product = newProduct();
            product.clearDomainEvents();

            product.update(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_CATEGORY, VALID_IMAGE);

            assertEquals(1, product.getDomainEvents().size());
            assertInstanceOf(ProductUpdated.class, product.getDomainEvents().get(0));
        }
    }

    @Nested
    @DisplayName("incrementStock")
    class IncrementStock {

        @Test
        @DisplayName("Should increment stock by the given quantity")
        void shouldIncrementStockByGivenQuantity() {
            Product product = newProduct();

            product.incrementStock(5, "Restock");

            assertEquals(15, product.getStock().value());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when reason is blank")
        void shouldThrowIllegalArgumentExceptionWhenReasonIsBlank() {
            Product product = newProduct();

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> product.incrementStock(5, "  "));

            assertEquals("Reason for stock increment cannot be null or blank", exception.getMessage());
        }

        @Test
        @DisplayName("Should register a StockChanged event with old and new stock values")
        void shouldRegisterStockChangedEventWithOldAndNewStockValues() {
            Product product = newProduct();
            product.clearDomainEvents();

            product.incrementStock(5, "Restock");

            assertEquals(1, product.getDomainEvents().size());
            StockChanged event = (StockChanged) product.getDomainEvents().get(0);
            assertEquals(10, event.oldStock());
            assertEquals(15, event.newStock());
        }
    }

    @Nested
    @DisplayName("decrementStock")
    class DecrementStock {

        @Test
        @DisplayName("Should decrement stock by the given quantity")
        void shouldDecrementStockByGivenQuantity() {
            Product product = newProduct();

            product.decrementStock(4, "Sale");

            assertEquals(6, product.getStock().value());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when reason is blank")
        void shouldThrowIllegalArgumentExceptionWhenReasonIsBlank() {
            Product product = newProduct();

            assertThrows(IllegalArgumentException.class, () -> product.decrementStock(4, ""));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when stock is insufficient")
        void shouldThrowIllegalArgumentExceptionWhenStockIsInsufficient() {
            Product product = newProduct();

            assertThrows(IllegalArgumentException.class, () -> product.decrementStock(100, "Sale"));
        }

        @Test
        @DisplayName("Should register a StockChanged event with old and new stock values")
        void shouldRegisterStockChangedEventWithOldAndNewStockValues() {
            Product product = newProduct();
            product.clearDomainEvents();

            product.decrementStock(4, "Sale");

            StockChanged event = (StockChanged) product.getDomainEvents().get(0);
            assertEquals(10, event.oldStock());
            assertEquals(6, event.newStock());
        }
    }

    @Nested
    @DisplayName("changePrice")
    class ChangePrice {

        @Test
        @DisplayName("Should update the product price")
        void shouldUpdateProductPrice() {
            Product product = newProduct();
            Money newPrice = Money.of(BigDecimal.valueOf(2000), USD);

            product.changePrice(newPrice);

            assertEquals(newPrice, product.getPrice());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when new price is invalid")
        void shouldThrowIllegalArgumentExceptionWhenNewPriceIsInvalid() {
            Product product = newProduct();

            assertThrows(IllegalArgumentException.class,
                    () -> product.changePrice(Money.of(BigDecimal.ZERO, USD)));
        }

        @Test
        @DisplayName("Should register a ProductUpdated event")
        void shouldRegisterProductUpdatedEvent() {
            Product product = newProduct();
            product.clearDomainEvents();

            product.changePrice(Money.of(BigDecimal.valueOf(2000), USD));

            assertInstanceOf(ProductUpdated.class, product.getDomainEvents().get(0));
        }
    }

    @Nested
    @DisplayName("deactivate")
    class Deactivate {

        @Test
        @DisplayName("Should deactivate an active product")
        void shouldDeactivateActiveProduct() {
            Product product = newProduct();

            product.deactivate();

            assertFalse(product.isActive());
        }

        @Test
        @DisplayName("Should throw IllegalStateException when product is already deactivated")
        void shouldThrowIllegalStateExceptionWhenAlreadyDeactivated() {
            Product product = newProduct();
            product.deactivate();

            assertThrows(IllegalStateException.class, product::deactivate);
        }

        @Test
        @DisplayName("Should register a ProductDeactivated event")
        void shouldRegisterProductDeactivatedEvent() {
            Product product = newProduct();
            product.clearDomainEvents();

            product.deactivate();

            assertInstanceOf(ProductDeactivated.class, product.getDomainEvents().get(0));
        }
    }

    @Nested
    @DisplayName("activate")
    class Activate {

        @Test
        @DisplayName("Should activate an inactive product")
        void shouldActivateInactiveProduct() {
            Product product = newProduct();
            product.deactivate();

            product.activate();

            assertTrue(product.isActive());
        }

        @Test
        @DisplayName("Should throw IllegalStateException when product is already active")
        void shouldThrowIllegalStateExceptionWhenAlreadyActive() {
            Product product = newProduct();

            assertThrows(IllegalStateException.class, product::activate);
        }
    }

    @Nested
    @DisplayName("hasAvailableStock")
    class HasAvailableStock {

        @Test
        @DisplayName("Should return true when product is active and has sufficient stock")
        void shouldReturnTrueWhenActiveAndSufficientStock() {
            Product product = newProduct();

            assertTrue(product.hasAvailableStock(5));
        }

        @Test
        @DisplayName("Should return false when product is inactive")
        void shouldReturnFalseWhenInactive() {
            Product product = newProduct();
            product.deactivate();

            assertFalse(product.hasAvailableStock(5));
        }

        @Test
        @DisplayName("Should return false when stock is insufficient")
        void shouldReturnFalseWhenStockIsInsufficient() {
            Product product = newProduct();

            assertFalse(product.hasAvailableStock(100));
        }
    }
}
