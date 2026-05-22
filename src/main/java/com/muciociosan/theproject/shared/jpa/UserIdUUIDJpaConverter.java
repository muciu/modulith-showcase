package com.muciociosan.theproject.shared.jpa;

import com.muciociosan.theproject.shared.ids.UserId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.UUID;

@Converter
public class UserIdUUIDJpaConverter implements AttributeConverter<UserId, UUID> {

    @Override
    public UUID convertToDatabaseColumn(final UserId attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.value();
    }

    @Override
    public UserId convertToEntityAttribute(final UUID dbData) {
        if (dbData == null) {
            return null;
        }

        return UserId.from(dbData);
    }
}
