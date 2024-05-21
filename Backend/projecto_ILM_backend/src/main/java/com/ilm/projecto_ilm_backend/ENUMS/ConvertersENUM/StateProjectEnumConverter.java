package com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converter class to map between StateProjectENUM and its corresponding integer value in the database.
 * This converter allows the enum values to be stored as integers in the database.
 */
@Converter(autoApply = true)
public class StateProjectEnumConverter implements AttributeConverter<StateProjectENUM, Integer> {

    /**
     * Converts the StateProjectENUM attribute to its corresponding integer value for storing in the database.
     *
     * @param attribute the StateProjectENUM attribute to be converted
     * @return the integer value corresponding to the StateProjectENUM attribute, or null if the attribute is null
     */
    @Override
    public Integer convertToDatabaseColumn(StateProjectENUM attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getIntValue();
    }

    /**
     * Converts the integer value from the database to its corresponding StateProjectENUM attribute.
     *
     * @param dbData the integer value from the database
     * @return the corresponding StateProjectENUM attribute, or null if the dbData is null
     */
    @Override
    public StateProjectENUM convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return StateProjectENUM.fromInt(dbData);
    }
}
