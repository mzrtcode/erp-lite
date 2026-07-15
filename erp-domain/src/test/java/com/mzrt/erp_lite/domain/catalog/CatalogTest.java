package com.mzrt.erp_lite.domain.catalog;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Catalog DomainTest")
class CatalogTest {

    private static final String VALID_ID = "cat-electronics";
    private static final String VALID_NAME = "Product Categories";
    private static final String VALID_DESCRIPTION = "Categories for products";

    private static CatalogItem newItem(String code, boolean active) {
        CatalogItem item = new CatalogItem(
                "11111111-1111-1111-1111-111111111111",
                code,
                "Value " + code,
                "Description " + code,
                1,
                Map.of()
        );
        if (!active) {
            item.turnOffStatus();
        }
        return item;
    }

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidation {

        @Test
        @DisplayName("Should throw IllegalArgumentException when catalogType is null")
        void shouldThrowIllegalArgumentExceptionWhenCatalogTypeIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    new Catalog(VALID_ID, null, VALID_NAME, VALID_DESCRIPTION, List.of(), true));

            assertEquals("CatalogType cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when name is null")
        void shouldThrowIllegalArgumentExceptionWhenNameIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    new Catalog(VALID_ID, CatalogType.PRODUCT_CATEGORIES, null, VALID_DESCRIPTION, List.of(), true));

            assertEquals("Name cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should create a Catalog exposing all provided fields")
        void shouldCreateCatalogWithAllProvidedFields() {
            List<CatalogItem> items = List.of(newItem("ACTIVE", true));

            Catalog catalog = new Catalog(VALID_ID, CatalogType.PRODUCT_CATEGORIES, VALID_NAME, VALID_DESCRIPTION, items, true);

            assertEquals(VALID_ID, catalog.getId());
            assertEquals(CatalogType.PRODUCT_CATEGORIES, catalog.getCatalogType());
            assertEquals(VALID_NAME, catalog.getName());
            assertEquals(VALID_DESCRIPTION, catalog.getDescription());
            assertEquals(items, catalog.getItems());
            assertTrue(catalog.isActive());
        }
    }

    @Nested
    @DisplayName("containsItem")
    class ContainsItem {

        @Test
        @DisplayName("Should return true when an item with the given code exists")
        void shouldReturnTrueWhenItemWithCodeExists() {
            Catalog catalog = new Catalog(VALID_ID, CatalogType.PRODUCT_CATEGORIES, VALID_NAME, VALID_DESCRIPTION,
                    List.of(newItem("ACTIVE", true)), true);

            assertTrue(catalog.containsItem("ACTIVE"));
        }

        @Test
        @DisplayName("Should return false when no item with the given code exists")
        void shouldReturnFalseWhenNoItemWithCodeExists() {
            Catalog catalog = new Catalog(VALID_ID, CatalogType.PRODUCT_CATEGORIES, VALID_NAME, VALID_DESCRIPTION,
                    List.of(newItem("ACTIVE", true)), true);

            assertFalse(catalog.containsItem("MISSING"));
        }
    }

    @Nested
    @DisplayName("findItemsActive")
    class FindItemsActive {

        @Test
        @DisplayName("Should return only active items")
        void shouldReturnOnlyActiveItems() {
            CatalogItem active = newItem("ACTIVE", true);
            CatalogItem inactive = newItem("INACTIVE", false);
            Catalog catalog = new Catalog(VALID_ID, CatalogType.PRODUCT_CATEGORIES, VALID_NAME, VALID_DESCRIPTION,
                    List.of(active, inactive), true);

            List<CatalogItem> activeItems = catalog.findItemsActive();

            assertEquals(List.of(active), activeItems);
        }
    }

    @Nested
    @DisplayName("findAll")
    class FindAll {

        @Test
        @DisplayName("Should return all items regardless of status")
        void shouldReturnAllItemsRegardlessOfStatus() {
            CatalogItem active = newItem("ACTIVE", true);
            CatalogItem inactive = newItem("INACTIVE", false);
            Catalog catalog = new Catalog(VALID_ID, CatalogType.PRODUCT_CATEGORIES, VALID_NAME, VALID_DESCRIPTION,
                    List.of(active, inactive), true);

            assertEquals(List.of(active, inactive), catalog.findAll());
        }

        @Test
        @DisplayName("Should return an unmodifiable list")
        void shouldReturnUnmodifiableList() {
            Catalog catalog = new Catalog(VALID_ID, CatalogType.PRODUCT_CATEGORIES, VALID_NAME, VALID_DESCRIPTION,
                    List.of(newItem("ACTIVE", true)), true);

            assertThrows(UnsupportedOperationException.class,
                    () -> catalog.findAll().add(newItem("OTHER", true)));
        }
    }
}
