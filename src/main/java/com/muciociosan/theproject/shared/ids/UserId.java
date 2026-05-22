package com.muciociosan.theproject.shared.ids;

import java.util.UUID;

import static java.util.Objects.isNull;

public class UserId extends ScalarValue<UUID> implements UUIDBasedId {

    private UserId(final UUID id) {
        super(id);
    }

    public static UserId random() {
        return from(UUID.randomUUID());
    }

    public static UserId from(final UUID id) {
        return new UserId(id);
    }

    public static UserId fromNullable(UUID id) {
        if (isNull(id)) {
            return null;
        }
        return UserId.from(id);
    }

    @Override
    public UUID uuid() {
        return value;
    }
}
