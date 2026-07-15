package com.mzrt.erp_lite.domain.catalog;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CatalogItem DomainTest")
class CatalogItemTest {

    private static final String VALID_ID = "11111111-1111-1111-1111-111111111111";
    private static final String VALID_CODE = "ACTIVE";
    private static final String VALID_VALUE = "Active";
    private static final String VALID_DESCRIPTION = "Item is active";
    private static final int VALID_DISPLAY_ORDER = 1;

    private static CatalogItem newCatalogItem(Map<String, Object> metadata) {
        return new CatalogItem(
                VALID_ID,
                VALID_CODE,
                VALID_VALUE,
                VALID_DESCRIPTION,
                VALID_DISPLAY_ORDER,
                metadata);
    }

    private static CatalogItem newCatalogItem() {
        return newCatalogItem(Map.of("displayColor", "green"));
    }

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidation {

        @Test
        @DisplayName("Should throw IllegalArgumentException when code is empty")
        void shouldThrowIllegalArgumentExceptionWhenCodeIsBlank() {
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () ->
                    new CatalogItem(
                            UUID.randomUUID().toString(),
                            "",
                            "Some value",
                            "This product is a test",
                            1,
                            Map.of("Value display", "Test")
                    ));

            assertEquals("Code cannot be null or empty", illegalArgumentException.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when code is null")
        void shouldThrowIllegalArgumentExceptionWhenCodeIsNull() {
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () ->
                    new CatalogItem(
                            UUID.randomUUID().toString(),
                            null,
                            VALID_VALUE,
                            VALID_DESCRIPTION,
                            VALID_DISPLAY_ORDER,
                            Map.of()
                    ));

            assertEquals("Code cannot be null or empty", illegalArgumentException.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when id is null")
        void shouldThrowIllegalArgumentExceptionWhenIdIsNull() {
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () ->
                    new CatalogItem(
                            null,
                            VALID_CODE,
                            VALID_VALUE,
                            VALID_DESCRIPTION,
                            VALID_DISPLAY_ORDER,
                            Map.of()
                    ));

            assertEquals("Entity ID cannot be null", illegalArgumentException.getMessage());
        }
    }

    @Nested
    @DisplayName("Successful creation")
    class Creation {

        @Test
        @DisplayName("Should create a CatalogItem exposing all provided fields")
        void shouldCreateCatalogItemWithAllFieldsWhenValidDataProvided() {
            Map<String, Object> metadata = Map.of("displayColor", "green");

            CatalogItem catalogItem = new CatalogItem(
                    VALID_ID,
                    VALID_CODE,
                    VALID_VALUE,
                    VALID_DESCRIPTION,
                    VALID_DISPLAY_ORDER,
                    metadata);

            assertEquals(VALID_ID, catalogItem.getId());
            assertEquals(VALID_CODE, catalogItem.getCode());
            assertEquals(VALID_VALUE, catalogItem.getValue());
            assertEquals(VALID_DESCRIPTION, catalogItem.getDescription());
            assertEquals(VALID_DISPLAY_ORDER, catalogItem.getDisplayOrder());
            assertEquals(metadata, catalogItem.getMetadata());
        }

        @Test
        @DisplayName("Should default isActive to true when created")
        void shouldDefaultIsActiveToTrueWhenCreated() {
            CatalogItem catalogItem = newCatalogItem();

            assertTrue(catalogItem.isActive());
        }

        @Test
        @DisplayName("Should use an empty map when metadata is null")
        void shouldUseEmptyMapWhenMetadataIsNull() {
            CatalogItem catalogItem = newCatalogItem(null);

            assertTrue(catalogItem.getMetadata().isEmpty());
        }

        @Test
        @DisplayName("Should defensively copy the provided metadata map")
        void shouldCopyMetadataDefensivelyWhenMetadataIsProvided() {
            Map<String, Object> originalMetadata = new HashMap<>();
            originalMetadata.put("displayColor", "green");

            CatalogItem catalogItem = newCatalogItem(originalMetadata);
            originalMetadata.put("displayColor", "red");
            originalMetadata.put("newKey", "newValue");

            assertEquals("green", catalogItem.getMetadata("displayColor"));
            assertFalse(catalogItem.hasMetadata("newKey"));
        }

        @Test
        @DisplayName("Should expose an immutable metadata map")
        void shouldReturnImmutableMetadataMap() {
            CatalogItem catalogItem = newCatalogItem();

            assertThrows(UnsupportedOperationException.class,
                    () -> catalogItem.getMetadata().put("newKey", "newValue"));
        }
    }

    @Nested
    @DisplayName("Metadata access")
    class MetadataAccess {

        @Test
        @DisplayName("Should return the metadata value when the key exists")
        void shouldReturnMetadataValueWhenKeyExists() {
            CatalogItem catalogItem = newCatalogItem(Map.of("displayColor", "green"));

            assertEquals("green", catalogItem.getMetadata("displayColor"));
        }

        @Test
        @DisplayName("Should return null when the metadata key does not exist")
        void shouldReturnNullWhenMetadataKeyDoesNotExist() {
            CatalogItem catalogItem = newCatalogItem(Map.of("displayColor", "green"));

            assertNull(catalogItem.getMetadata("missingKey"));
        }

        @Test
        @DisplayName("Should return true when the metadata key exists")
        void shouldReturnTrueWhenMetadataKeyExists() {
            CatalogItem catalogItem = newCatalogItem(Map.of("displayColor", "green"));

            assertTrue(catalogItem.hasMetadata("displayColor"));
        }

        @Test
        @DisplayName("Should return false when the metadata key does not exist")
        void shouldReturnFalseWhenMetadataKeyDoesNotExist() {
            CatalogItem catalogItem = newCatalogItem(Map.of("displayColor", "green"));

            assertFalse(catalogItem.hasMetadata("missingKey"));
        }
    }

    @Nested
    @DisplayName("Status transitions")
    class StatusTransitions {

        @Test
        @DisplayName("Should turn off the status of an active item")
        void shouldTurnOffStatus() {
            CatalogItem catalogItem = newCatalogItem();

            catalogItem.turnOffStatus();

            assertFalse(catalogItem.isActive());
        }

        @Test
        @DisplayName("Should turn on the status of an inactive item")
        void shouldTurnOnStatus() {
            CatalogItem catalogItem = newCatalogItem();
            catalogItem.turnOffStatus();

            catalogItem.turnOnStatus();

            assertTrue(catalogItem.isActive());
        }
    }

    @Nested
    @DisplayName("equals, hashCode and toString")
    class ObjectContract {

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            CatalogItem catalogItem = newCatalogItem();

            assertEquals(catalogItem, catalogItem);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            CatalogItem catalogItem = newCatalogItem();

            assertNotEquals(null, catalogItem);
        }

        @Test
        @DisplayName("Should not be equal to an instance of a different type")
        void shouldNotBeEqualToDifferentType() {
            CatalogItem catalogItem = newCatalogItem();

            assertNotEquals("not-a-catalog-item", catalogItem);
        }

        @Test
        @DisplayName("Should not be equal when the code differs")
        void shouldNotBeEqualWhenCodeDiffers() {
            CatalogItem first = new CatalogItem(VALID_ID, "ACTIVE", VALID_VALUE, VALID_DESCRIPTION, VALID_DISPLAY_ORDER, Map.of());
            CatalogItem second = new CatalogItem(VALID_ID, "INACTIVE", VALID_VALUE, VALID_DESCRIPTION, VALID_DISPLAY_ORDER, Map.of());

            assertNotEquals(first, second);
        }

        @Test
        @DisplayName("Should be equal and share hashCode when every field matches")
        void shouldBeEqualAndShareHashCodeWhenAllFieldsMatch() {
            CatalogItem first = newCatalogItem(Map.of("displayColor", "green"));
            CatalogItem second = newCatalogItem(Map.of("displayColor", "green"));

            assertEquals(first, second);
            assertEquals(first.hashCode(), second.hashCode());
        }

        @Test
        @DisplayName("Should include the item's fields in toString")
        void shouldIncludeKeyFieldsInToString() {
            CatalogItem catalogItem = newCatalogItem();

            String result = catalogItem.toString();

            assertTrue(result.contains(VALID_CODE));
            assertTrue(result.contains(VALID_VALUE));
        }
    }
}
