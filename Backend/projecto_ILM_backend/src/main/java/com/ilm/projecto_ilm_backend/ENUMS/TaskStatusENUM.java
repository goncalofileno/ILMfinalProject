package com.ilm.projecto_ilm_backend.ENUMS;

/**
 * Enum representing different statuses of a task.
 */
public enum TaskStatusENUM {
    /**
     * Pending status.
     * Represented by the integer value 0.
     */
    PLANNED(0),

    /**
     * In-progress status.
     * Represented by the integer value 1.
     */
    IN_PROGRESS(1),

    /**
     * Done status.
     * Represented by the integer value 2.
     */
    DONE(2);

    private final int intValue;

    /**
     * Constructor for TaskStatusENUM.
     * Associates an integer value with the enum constant.
     *
     * @param intValue the integer value associated with the enum constant
     */
    TaskStatusENUM(int intValue) {
        this.intValue = intValue;
    }

    /**
     * Gets the integer value associated with the enum constant.
     *
     * @return the integer value associated with the enum constant
     */
    public int getIntValue() {
        return intValue;
    }

    /**
     * Converts an integer value to the corresponding TaskStatusENUM constant.
     *
     * @param intValue the integer value to convert
     * @return the corresponding TaskStatusENUM constant
     * @throws IllegalArgumentException if the integer value does not correspond to any TaskStatusENUM constant
     */
    public static TaskStatusENUM fromInt(int intValue) {
        for (TaskStatusENUM status : TaskStatusENUM.values()) {
            if (status.getIntValue() == intValue) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid integer value for TaskStatusENUM: " + intValue);
    }
}

