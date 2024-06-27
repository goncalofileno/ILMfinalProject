package com.ilm.projecto_ilm_backend.ENUMS;

/**
 * Enum representing different types of notifications.
 */
public enum NotificationTypeENUM {

    PROJECT(0),

    INVITE(1),

    INVITE_ACCEPTED(2),

    INVITE_REJECTED(3),

    TASK(4),

    APPLIANCE(5),

    APPLIANCE_ACCEPTED(6),

    APPLIANCE_REJECTED(7),

    REMOVED(8),

    PROJECT_REJECTED(9),

    PROJECT_MESSAGE(10);

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
