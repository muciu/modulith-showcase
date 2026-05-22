package com.muciociosan.theproject.shared.ids;

import java.util.UUID;

public class TenantId extends ScalarValue<UUID> implements UUIDBasedId {

    private TenantId(final UUID id) {
        super(id);
    }

    public static TenantId from(final UUID id) {
        return new TenantId(id);
    }

    public static TenantId nullableFrom(final UUID id) {
        return new TenantId(id);
    }

    @Override
    public UUID uuid() {
        return value;
    }
}
