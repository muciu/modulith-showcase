package com.muciociosan.theproject.shared.exceptions;

import com.muciociosan.theproject.shared.ids.UUIDBasedId;

public class ResourceNotFoundException extends ApplicationException {
    public ResourceNotFoundException(final Class<?> type, final UUIDBasedId id) {
        super("RESOURCE_NOT_FOUND", "Resource of type %s and ID %s not found".formatted(type.getSimpleName(), id.uuid().toString()));
    }
}
