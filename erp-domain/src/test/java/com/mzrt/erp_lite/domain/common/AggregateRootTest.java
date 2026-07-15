package com.mzrt.erp_lite.domain.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AggregateRoot DomainTest")
class AggregateRootTest {

    private record TestEvent(String data) implements DomainEvent {
    }

    private static class TestAggregate extends AggregateRoot<String> {
        TestAggregate(String id) {
            super(id);
        }

        void raise(DomainEvent event) {
            registerEvent(event);
        }
    }

    @Nested
    @DisplayName("Domain events registration")
    class EventRegistration {

        @Test
        @DisplayName("Should have no domain events when created")
        void shouldHaveNoDomainEventsWhenCreated() {
            TestAggregate aggregate = new TestAggregate("id-1");

            assertTrue(aggregate.getDomainEvents().isEmpty());
        }

        @Test
        @DisplayName("Should register a domain event")
        void shouldRegisterDomainEvent() {
            TestAggregate aggregate = new TestAggregate("id-1");
            TestEvent event = new TestEvent("something happened");

            aggregate.raise(event);

            assertEquals(List.of(event), aggregate.getDomainEvents());
        }

        @Test
        @DisplayName("Should ignore null events")
        void shouldIgnoreNullEvents() {
            TestAggregate aggregate = new TestAggregate("id-1");

            aggregate.raise(null);

            assertTrue(aggregate.getDomainEvents().isEmpty());
        }

        @Test
        @DisplayName("Should keep registration order for multiple events")
        void shouldKeepRegistrationOrderForMultipleEvents() {
            TestAggregate aggregate = new TestAggregate("id-1");
            TestEvent first = new TestEvent("first");
            TestEvent second = new TestEvent("second");

            aggregate.raise(first);
            aggregate.raise(second);

            assertEquals(List.of(first, second), aggregate.getDomainEvents());
        }

        @Test
        @DisplayName("Should return an unmodifiable list of domain events")
        void shouldReturnUnmodifiableDomainEventsList() {
            TestAggregate aggregate = new TestAggregate("id-1");
            aggregate.raise(new TestEvent("something happened"));

            assertThrows(UnsupportedOperationException.class,
                    () -> aggregate.getDomainEvents().add(new TestEvent("other")));
        }
    }

    @Nested
    @DisplayName("Clearing domain events")
    class ClearingEvents {

        @Test
        @DisplayName("Should clear all registered domain events")
        void shouldClearAllRegisteredDomainEvents() {
            TestAggregate aggregate = new TestAggregate("id-1");
            aggregate.raise(new TestEvent("something happened"));

            aggregate.clearDomainEvents();

            assertTrue(aggregate.getDomainEvents().isEmpty());
        }
    }
}
