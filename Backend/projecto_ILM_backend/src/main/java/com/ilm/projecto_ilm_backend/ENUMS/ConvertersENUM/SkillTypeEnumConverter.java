package com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM;

import com.ilm.projecto_ilm_backend.ENUMS.SkillTypeENUM;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converter class to map between SkillTypeENUM and its corresponding integer value in the database.
 * This converter allows the enum values to be stored as integers in the database.
 */
@Converter(autoApply = true)
public class SkillTypeEnumConverter implements AttributeConverter<SkillTypeENUM, Integer> {

    /**
     * Converts the SkillTypeENUM attribute to its corresponding integer value for storing in the database.
     *
     * @param attribute the SkillTypeENUM attribute to be converted
     * @return the integer value corresponding to the SkillTypeENUM attribute, or null if the attribute is null
     */
    @Override
    public Integer convertToDatabaseColumn(SkillTypeENUM attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getIntValue();
    }

    /**
     * Converts the integer value from the database to its corresponding SkillTypeENUM attribute.
     *
     * @param dbData the integer value from the database
     * @return the corresponding SkillTypeENUM attribute, or null if the dbData is null
     */
    @Override
    public SkillTypeENUM convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return SkillTypeENUM.fromInt(dbData);
    }
}

