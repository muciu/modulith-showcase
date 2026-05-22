package com.muciociosan.theproject.shared.exceptions;

import lombok.Getter;

import java.util.regex.Pattern;

public class ApplicationException extends RuntimeException {
    private static final Pattern CODE_PATTERN = Pattern.compile("[A-Z](?:[A-Z0-9_]*[A-Z])?");

    @Getter
    private final String code;

    public ApplicationException(final String message) {
        this("INTERNAL_SERVER_ERROR", message);
    }

    public ApplicationException(final String code, final String message) {
        super(message);
        this.code = validateCodeFormat(code);
    }

    private static String validateCodeFormat(final String code) {
        if (code == null || !CODE_PATTERN.matcher(code).matches()) {
            throw new IllegalArgumentException(
                "Code must contain only uppercase letters, digits, and underscores, and start and end with a letter."
            );
        }
        return code;
    }
}
