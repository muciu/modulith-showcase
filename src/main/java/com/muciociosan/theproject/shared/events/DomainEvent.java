package com.muciociosan.theproject.shared.events;

import java.util.Map;

/**
 * Intended to be a cross-domain event (a base class)
 */
public abstract class DomainEvent<T> {

    private T identity;
    private Map<String, Object> details;

    public DomainEvent(final T identity) {
        this(identity, Map.of());
    }

    public DomainEvent(final T identity, final Map<String, Object> details) {
        this.identity = identity;
        this.details = Map.copyOf(details);
    }

    public T identity() {
        return identity;
    }

    public Map<String, Object> details() {
        return Map.copyOf(details);
    }

}
