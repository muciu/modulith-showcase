package com.muciociosan.theproject.shared.ids;

import java.util.UUID;

import static java.util.Objects.isNull;

public class EmailId extends ScalarValue<UUID> implements UUIDBasedId {

    private EmailId(final UUID id) {
        super(id);
    }

    public static EmailId from(final UUID id) {
        return new EmailId(id);
    }

    public static EmailId fromNullable(UUID id) {
        if (isNull(id)) {
            return null;
        }
        return EmailId.from(id);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof EmailId userId)) {
            return false;
        }
        return value.equals(userId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public UUID uuid() {
        return value;
    }
}
