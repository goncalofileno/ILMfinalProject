package com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM;

import com.ilm.projecto_ilm_backend.ENUMS.UserInTaskTypeENUM;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converter class to map between UserInTaskTypeENUM and its corresponding integer value in the database.
 * This converter allows the enum values to be stored as integers in the database.
 */
@Converter(autoApply = true)
public class UserInTaskTypeEnumConverter implements AttributeConverter<UserInTaskTypeENUM, Integer> {

    /**
     * Converts the UserInTaskTypeENUM attribute to its corresponding integer value for storing in the database.
     *
     * @param attribute the UserInTaskTypeENUM attribute to be converted
     * @return the integer value corresponding to the UserInTaskTypeENUM attribute, or null if the attribute is null
     */
    @Override
    public Integer convertToDatabaseColumn(UserInTaskTypeENUM attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getIntValue();
    }

    /**
     * Converts the integer value from the database to its corresponding UserInTaskTypeENUM attribute.
     *
     * @param dbData the integer value from the database
     * @return the corresponding UserInTaskTypeENUM attribute, or null if the dbData is null
     */
    @Override
    public UserInTaskTypeENUM convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return UserInTaskTypeENUM.fromInt(dbData);
    }
}

