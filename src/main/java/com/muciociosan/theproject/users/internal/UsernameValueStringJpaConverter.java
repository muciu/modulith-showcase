package com.muciociosan.theproject.users.internal;

import com.muciociosan.theproject.users.domain.UsernameValue;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UsernameValueStringJpaConverter implements AttributeConverter<UsernameValue, String> {

    @Override
    public String convertToDatabaseColumn(UsernameValue attribute) {
        return attribute.value();
    }

    @Override
    public UsernameValue convertToEntityAttribute(String dbData) {
        return UsernameValue.fromNullable(dbData);
    }
}
