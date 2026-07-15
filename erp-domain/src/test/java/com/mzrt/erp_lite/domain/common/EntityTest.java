package com.mzrt.erp_lite.domain.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Entity DomainTest")
class EntityTest {

    private static class TestEntity extends Entity<String> {
        TestEntity(String id) {
            super(id);
        }
    }

    private static class OtherTestEntity extends Entity<String> {
        OtherTestEntity(String id) {
            super(id);
        }
    }

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidation {

        @Test
        @DisplayName("Should throw IllegalArgumentException when id is null")
        void shouldThrowIllegalArgumentExceptionWhenIdIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new TestEntity(null));

            assertEquals("Entity ID cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should create the entity exposing the provided id")
        void shouldCreateEntityWithProvidedId() {
            TestEntity entity = new TestEntity("id-1");

            assertEquals("id-1", entity.getId());
        }
    }

    @Nested
    @DisplayName("equals and hashCode")
    class ObjectContract {

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            TestEntity entity = new TestEntity("id-1");

            assertEquals(entity, entity);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            TestEntity entity = new TestEntity("id-1");

            assertNotEquals(null, entity);
        }

        @Test
        @DisplayName("Should not be equal to an instance of a different type")
        void shouldNotBeEqualToDifferentType() {
            TestEntity entity = new TestEntity("id-1");

            assertNotEquals("not-an-entity", entity);
        }

        @Test
        @DisplayName("Should be equal and share hashCode when ids match")
        void shouldBeEqualWhenIdsMatch() {
            TestEntity first = new TestEntity("id-1");
            TestEntity second = new TestEntity("id-1");

            assertEquals(first, second);
            assertEquals(first.hashCode(), second.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when ids differ")
        void shouldNotBeEqualWhenIdsDiffer() {
            TestEntity first = new TestEntity("id-1");
            TestEntity second = new TestEntity("id-2");

            assertNotEquals(first, second);
        }

        @Test
        @DisplayName("Should not be equal to an entity of a different subtype sharing the same id")
        void shouldNotBeEqualToDifferentSubtypeWithSameId() {
            TestEntity entity = new TestEntity("id-1");
            OtherTestEntity other = new OtherTestEntity("id-1");

            assertNotEquals(entity, other);
        }
    }
}
