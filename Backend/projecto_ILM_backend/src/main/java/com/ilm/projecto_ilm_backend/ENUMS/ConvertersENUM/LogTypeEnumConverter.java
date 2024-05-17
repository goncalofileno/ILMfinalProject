package com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM;

import com.ilm.projecto_ilm_backend.ENUMS.LogTypeENUM;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converter class to map between LogTypeENUM and its corresponding integer value in the database.
 * This converter allows the enum values to be stored as integers in the database.
 */
@Converter(autoApply = true)
public class LogTypeEnumConverter implements AttributeConverter<LogTypeENUM, Integer> {

    /**
     * Converts the LogTypeENUM attribute to its corresponding integer value for storing in the database.
     *
     * @param attribute the LogTypeENUM attribute to be converted
     * @return the integer value corresponding to the LogTypeENUM attribute, or null if the attribute is null
     */
    @Override
    public Integer convertToDatabaseColumn(LogTypeENUM attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getIntValue();
    }

    /**
     * Converts the integer value from the database to its corresponding LogTypeENUM attribute.
     *
     * @param dbData the integer value from the database
     * @return the corresponding LogTypeENUM attribute, or null if the dbData is null
     */
    @Override
    public LogTypeENUM convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return LogTypeENUM.fromInt(dbData);
    }
}

