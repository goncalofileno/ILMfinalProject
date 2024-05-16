package com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM;

import com.ilm.projecto_ilm_backend.ENUMS.TaskStatusENUM;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converter class to map between TaskStatusENUM and its corresponding integer value in the database.
 * This converter allows the enum values to be stored as integers in the database.
 */
@Converter(autoApply = true)
public class TaskStatusEnumConverter implements AttributeConverter<TaskStatusENUM, Integer> {

    /**
     * Converts the TaskStatusENUM attribute to its corresponding integer value for storing in the database.
     *
     * @param attribute the TaskStatusENUM attribute to be converted
     * @return the integer value corresponding to the TaskStatusENUM attribute, or null if the attribute is null
     */
    @Override
    public Integer convertToDatabaseColumn(TaskStatusENUM attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getIntValue();
    }

    /**
     * Converts the integer value from the database to its corresponding TaskStatusENUM attribute.
     *
     * @param dbData the integer value from the database
     * @return the corresponding TaskStatusENUM attribute, or null if the dbData is null
     */
    @Override
    public TaskStatusENUM convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return TaskStatusENUM.fromInt(dbData);
    }
}

