package com.muciociosan.theproject.users.domain;

import com.muciociosan.theproject.shared.exceptions.ValidationException;
import com.muciociosan.theproject.shared.ids.ScalarValue;
import org.jmolecules.ddd.annotation.ValueObject;
import org.jspecify.annotations.Nullable;

import java.util.regex.Pattern;

import static java.util.Objects.isNull;

@ValueObject
public class UsernameValue extends ScalarValue<String> {
    private static final Pattern USERNAME_PATTERN = Pattern.compile("[A-Za-z0-9._-]+");

    public UsernameValue(final String username) {
        super(validUsername(username));
    }

    private static String validUsername(final String username) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("username", "Username must not be blank.");
        }
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new ValidationException("username",
                    "Username may contain only letters, digits, dots, dashes, and underscores."
            );
        }
        return username;
    }

    public static UsernameValue from(String value) {
        return new UsernameValue(value);
    }

    public static UsernameValue fromNullable(@Nullable String nullable) {
        if (isNull(nullable)) {
            return null;
        }
        return new UsernameValue(nullable);
    }
}
