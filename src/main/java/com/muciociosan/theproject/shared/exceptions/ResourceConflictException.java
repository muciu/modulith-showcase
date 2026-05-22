package com.muciociosan.theproject.shared.exceptions;

public class ResourceConflictException extends ApplicationException {
    public ResourceConflictException(final Class<?> type, final String qualifier) {
        super("CONFLICT", "Resource of type %s with qualifier %s already exists".formatted(type.getSimpleName(), qualifier));
    }
}
