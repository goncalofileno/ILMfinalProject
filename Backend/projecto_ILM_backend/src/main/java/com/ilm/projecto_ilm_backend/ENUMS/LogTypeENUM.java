package com.ilm.projecto_ilm_backend.ENUMS;

/**
 * Enum representing different types of logs.
 */
public enum LogTypeENUM {
    /**
     * Log for members.
     * Represented by the integer value 0.
     */
    MEMBER_ADDED(0),

    MEMBER_REMOVED(9),

    /**
     * Log for tasks created.
     * Represented by the integer value 1.
     */
    TASKS_CREATED(1),

    /**
     * Log for tasks completed.
     * Represented by the integer value 2.
     */
    TASKS_COMPLETED(2),

    /**
     * Log for tasks in progress.
     * Represented by the integer value 3.
     */
    TASKS_IN_PROGRESS(3),

    /**
     * Log for tasks deleted.
     * Represented by the integer value 4.
     */
    TASKS_DELETED(4),

    /**
     * Log for tasks updated.
     * Represented by the integer value 5.
     */
    TASKS_UPDATED(5),

    /**
     * Log for project information updated.
     * Represented by the integer value 6.
     */
    PROJECT_INFO_UPDATED(6),

    /**
     * Log for project status updated.
     * Represented by the integer value 7.
     */
    PROJECT_STATUS_UPDATED(7),

    /**
     * Log for resources added.
     * Represented by the integer value 8.
     */
    RESOURCES_UPDATED(8),

    MEMBER_TYPE_CHANGED(10),

    MEMBER_LEFT(11);

    private final int intValue;

    /**
     * Constructor for LogTypeENUM.
     * Associates an integer value with the enum constant.
     *
     * @param intValue the integer value associated with the enum constant
     */
    LogTypeENUM(int intValue) {
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
     * Converts an integer value to the corresponding LogTypeENUM constant.
     *
     * @param intValue the integer value to convert
     * @return the corresponding LogTypeENUM constant
     * @throws IllegalArgumentException if the integer value does not correspond to any LogTypeENUM constant
     */
    public static LogTypeENUM fromInt(int intValue) {
        for (LogTypeENUM type : LogTypeENUM.values()) {
            if (type.getIntValue() == intValue) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid integer value for LogTypeENUM: " + intValue);
    }
}

