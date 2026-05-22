package com.muciociosan.theproject.shared.exceptions;

public class ValidationException extends ApplicationException {
    public ValidationException(String field, String message) {
        super("VALIDATION", "Validation failed on field: '%s' with message: '%s'".formatted(field, message));
    }
}
