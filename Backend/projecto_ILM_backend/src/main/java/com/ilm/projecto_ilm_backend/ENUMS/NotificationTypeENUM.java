package com.ilm.projecto_ilm_backend.ENUMS;

/**
 * Enum representing different types of notifications.
 */
public enum NotificationTypeENUM {
    /**
     * Notification for a project.
     * Represented by the integer value 0.
     */
    PROJECT(0),

    /**
     * Notification for an invite.
     * Represented by the integer value 1.
     */
    INVITE(1),

    /**
     * Notification for a task.
     * Represented by the integer value 2.
     */
    TASK(2),

    /**
     * Notification for a message.
     * Represented by the integer value 3.
     */
    MESSAGE(3);

    private final int intValue;

    /**
     * Constructor for NotificationTypeENUM.
     * Associates an integer value with the enum constant.
     *
     * @param intValue the integer value associated with the enum constant
     */
    NotificationTypeENUM(int intValue) {
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
     * Converts an integer value to the corresponding NotificationTypeENUM constant.
     *
     * @param intValue the integer value to convert
     * @return the corresponding NotificationTypeENUM constant
     * @throws IllegalArgumentException if the integer value does not correspond to any NotificationTypeENUM constant
     */
    public static NotificationTypeENUM fromInt(int intValue) {
        for (NotificationTypeENUM type : NotificationTypeENUM.values()) {
            if (type.getIntValue() == intValue) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid integer value for NotificationTypeENUM: " + intValue);
    }
}
