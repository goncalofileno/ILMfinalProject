package com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM;

import com.ilm.projecto_ilm_backend.ENUMS.LanguageENUM;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LanguageEnumConverter implements AttributeConverter<LanguageENUM, Integer> {

    @Override
    public Integer convertToDatabaseColumn(LanguageENUM attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getIntValue();
    }

    @Override
    public LanguageENUM convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return LanguageENUM.fromInt(dbData);
    }
}
