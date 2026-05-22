package com.muciociosan.theproject.users.usecases;

import com.muciociosan.theproject.shared.exceptions.ValidationException;
import com.muciociosan.theproject.shared.ids.ScalarValue;
import org.apache.commons.lang3.StringUtils;
import org.jmolecules.ddd.annotation.ValueObject;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.isNull;

@ValueObject
public class EmailValue extends ScalarValue<String> {

    public EmailValue(final String email) {
        super(validUsername(email));
    }

    private static String validUsername(final String email) {
        if (StringUtils.isBlank(email)) {
            throw new ValidationException("email", "Username must not be blank.");
        }
        if (email.indexOf('@') < 0) {
            // TODO enhance the validation logic
            throw new ValidationException("email", "Not a valid email");
        }
        return email;
    }

    public static EmailValue from(String email) {
        return new EmailValue(email);
    }

    public static EmailValue fromNullable(@Nullable String email) {
        if (isNull(email)) {
            return null;
        }
        return new EmailValue(email);
    }
}
