package com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM;

import com.ilm.projecto_ilm_backend.ENUMS.LanguageENUM;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
/**
 * Converter class for LanguageENUM.
 * Implements the AttributeConverter interface to provide methods for converting between LanguageENUM and Integer.
 * This converter is automatically applied to all entities in the persistence context.
 */
@Converter(autoApply = true)
public class LanguageEnumConverter implements AttributeConverter<LanguageENUM, Integer> {
    /**
     * Converts a LanguageENUM attribute to its corresponding integer value for storage in the database.
     *
     * @param attribute the LanguageENUM attribute to convert
     * @return the integer value of the LanguageENUM attribute, or null if the attribute is null
     */
    @Override
    public Integer convertToDatabaseColumn(LanguageENUM attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getIntValue();
    }
    /**
     * Converts an integer value from the database to its corresponding LanguageENUM attribute.
     *
     * @param dbData the integer value from the database
     * @return the LanguageENUM attribute corresponding to the integer value, or null if the integer value is null
     */
    @Override
    public LanguageENUM convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return LanguageENUM.fromInt(dbData);
    }
}
