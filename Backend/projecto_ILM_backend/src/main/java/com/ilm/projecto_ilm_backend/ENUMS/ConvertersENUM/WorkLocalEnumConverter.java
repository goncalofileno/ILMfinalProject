package com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM;

import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converter class to map between WorkLocalENUM and its corresponding integer value in the database.
 * This converter allows the enum values to be stored as integers in the database.
 */
@Converter(autoApply = true)
public class WorkLocalEnumConverter implements AttributeConverter<WorkLocalENUM, Integer> {

    /**
     * Converts the WorkLocalENUM attribute to its corresponding integer value for storing in the database.
     *
     * @param attribute the WorkLocalENUM attribute to be converted
     * @return the integer value corresponding to the WorkLocalENUM attribute, or null if the attribute is null
     */
    @Override
    public Integer convertToDatabaseColumn(WorkLocalENUM attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getIntValue();
    }

    /**
     * Converts the integer value from the database to its corresponding WorkLocalENUM attribute.
     *
     * @param dbData the integer value from the database
     * @return the corresponding WorkLocalENUM attribute, or null if the dbData is null
     */
    @Override
    public WorkLocalENUM convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return WorkLocalENUM.fromInt(dbData);
    }
}
