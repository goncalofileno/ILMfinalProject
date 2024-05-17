package com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM;

import com.ilm.projecto_ilm_backend.ENUMS.ResourceTypeENUM;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converter class to map between ResourceTypeENUM and its corresponding integer value in the database.
 * This converter allows the enum values to be stored as integers in the database.
 */
@Converter(autoApply = true)
public class ResourceTypeEnumConverter implements AttributeConverter<ResourceTypeENUM, Integer> {

    /**
     * Converts the ResourceTypeENUM attribute to its corresponding integer value for storing in the database.
     *
     * @param attribute the ResourceTypeENUM attribute to be converted
     * @return the integer value corresponding to the ResourceTypeENUM attribute, or null if the attribute is null
     */
    @Override
    public Integer convertToDatabaseColumn(ResourceTypeENUM attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getIntValue();
    }

    /**
     * Converts the integer value from the database to its corresponding ResourceTypeENUM attribute.
     *
     * @param dbData the integer value from the database
     * @return the corresponding ResourceTypeENUM attribute, or null if the dbData is null
     */
    @Override
    public ResourceTypeENUM convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return ResourceTypeENUM.fromInt(dbData);
    }
}
