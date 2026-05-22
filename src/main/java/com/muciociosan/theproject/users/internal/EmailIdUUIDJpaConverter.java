package com.muciociosan.theproject.users.internal;

import com.muciociosan.theproject.shared.ids.EmailId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.UUID;

@Converter(autoApply = true)
public class EmailIdUUIDJpaConverter implements AttributeConverter<EmailId, UUID> {

    @Override
    public UUID convertToDatabaseColumn(EmailId attribute) {
        return attribute.uuid();
    }

    @Override
    public EmailId convertToEntityAttribute(UUID dbData) {
        return EmailId.fromNullable(dbData);
    }
}
