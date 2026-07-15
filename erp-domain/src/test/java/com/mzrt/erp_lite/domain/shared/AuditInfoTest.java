package com.mzrt.erp_lite.domain.shared;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AuditInfo DomainTest")
class AuditInfoTest {

    private static final String CREATED_BY = "system";
    private static final Instant TIMESTAMP = Instant.parse("2026-01-01T00:00:00Z");

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidation {

        @Test
        @DisplayName("Should throw IllegalArgumentException when createdBy is null")
        void shouldThrowIllegalArgumentExceptionWhenCreatedByIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new AuditInfo(null, TIMESTAMP, TIMESTAMP));

            assertEquals("CreatedBy cannot be null or blank", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when createdBy is blank")
        void shouldThrowIllegalArgumentExceptionWhenCreatedByIsBlank() {
            assertThrows(IllegalArgumentException.class, () -> new AuditInfo("  ", TIMESTAMP, TIMESTAMP));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when createdAt is null")
        void shouldThrowIllegalArgumentExceptionWhenCreatedAtIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new AuditInfo(CREATED_BY, null, TIMESTAMP));

            assertEquals("CreatedAt cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when updatedAt is null")
        void shouldThrowIllegalArgumentExceptionWhenUpdatedAtIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new AuditInfo(CREATED_BY, TIMESTAMP, null));

            assertEquals("UpdatedAt cannot be null", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("Should create AuditInfo with matching createdAt and updatedAt")
        void shouldCreateAuditInfoWithMatchingCreatedAtAndUpdatedAt() {
            AuditInfo auditInfo = AuditInfo.create(CREATED_BY, TIMESTAMP);

            assertEquals(CREATED_BY, auditInfo.createdBy());
            assertEquals(TIMESTAMP, auditInfo.createdAt());
            assertEquals(TIMESTAMP, auditInfo.updatedAt());
        }
    }

    @Nested
    @DisplayName("updateTimestamp")
    class UpdateTimestamp {

        @Test
        @DisplayName("Should preserve createdBy and createdAt while refreshing updatedAt")
        void shouldPreserveCreatedByAndCreatedAtWhileRefreshingUpdatedAt() {
            AuditInfo original = AuditInfo.create(CREATED_BY, TIMESTAMP);

            AuditInfo updated = original.updateTimestamp();

            assertEquals(original.createdBy(), updated.createdBy());
            assertEquals(original.createdAt(), updated.createdAt());
            assertFalse(updated.updatedAt().isBefore(original.updatedAt()));
        }

        @Test
        @DisplayName("Should return a new instance rather than mutating the original")
        void shouldReturnNewInstanceRatherThanMutatingOriginal() {
            AuditInfo original = AuditInfo.create(CREATED_BY, TIMESTAMP);

            AuditInfo updated = original.updateTimestamp();

            assertNotSame(original, updated);
            assertEquals(TIMESTAMP, original.updatedAt());
        }
    }
}
