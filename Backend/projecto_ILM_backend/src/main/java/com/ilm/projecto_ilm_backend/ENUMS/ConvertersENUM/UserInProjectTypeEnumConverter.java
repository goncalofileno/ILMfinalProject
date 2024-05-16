package com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM;

import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converter class to map between UserInProjectTypeENUM and its corresponding integer value in the database.
 * This converter allows the enum values to be stored as integers in the database.
 */
@Converter(autoApply = true)
public class UserInProjectTypeEnumConverter implements AttributeConverter<UserInProjectTypeENUM, Integer> {

    /**
     * Converts the UserInProjectTypeENUM attribute to its corresponding integer value for storing in the database.
     *
     * @param attribute the UserInProjectTypeENUM attribute to be converted
     * @return the integer value corresponding to the UserInProjectTypeENUM attribute, or null if the attribute is null
     */
    @Override
    public Integer convertToDatabaseColumn(UserInProjectTypeENUM attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getIntValue();
    }

    /**
     * Converts the integer value from the database to its corresponding UserInProjectTypeENUM attribute.
     *
     * @param dbData the integer value from the database
     * @return the corresponding UserInProjectTypeENUM attribute, or null if the dbData is null
     */
    @Override
    public UserInProjectTypeENUM convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return UserInProjectTypeENUM.fromInt(dbData);
    }
}

