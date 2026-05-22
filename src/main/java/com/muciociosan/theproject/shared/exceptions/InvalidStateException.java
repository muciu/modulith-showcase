package com.muciociosan.theproject.shared.exceptions;

public class InvalidStateException extends ApplicationException {
    public InvalidStateException(String message) {
        super("INVALID_STATE", message);
    }
}
