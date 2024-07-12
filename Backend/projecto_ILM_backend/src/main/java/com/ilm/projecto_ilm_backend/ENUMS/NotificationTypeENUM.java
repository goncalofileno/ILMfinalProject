package com.ilm.projecto_ilm_backend.ENUMS;

/**
 * Enum representing different types of notifications.
 * Each notification type is associated with a unique integer value.
 */
public enum NotificationTypeENUM {
    /**
     * Represents a project notification. Associated with the integer value 0.
     */
    PROJECT(0),
    /**
     * Represents an invite notification. Associated with the integer value 1.
     */
    INVITE(1),
    /**
     * Represents an invite accepted notification. Associated with the integer value 2.
     */
    INVITE_ACCEPTED(2),
    /**
     * Represents an invite rejected notification. Associated with the integer value 3.
     */
    INVITE_REJECTED(3),
    /**
     * Represents a task notification. Associated with the integer value 4.
     */
    TASK(4),
    /**
     * Represents an appliance notification. Associated with the integer value 5.
     */
    APPLIANCE(5),
    /**
     * Represents an appliance accepted notification. Associated with the integer value 6.
     */
    APPLIANCE_ACCEPTED(6),
    /**
     * Represents an appliance rejected notification. Associated with the integer value 7.
     */
    APPLIANCE_REJECTED(7),
    /**
     * Represents a removed notification. Associated with the integer value 8.
     */
    REMOVED(8),
    /**
     * Represents a project rejected notification. Associated with the integer value 9.
     */
    PROJECT_REJECTED(9),
    /**
     * Represents a project message notification. Associated with the integer value 10.
     */
    PROJECT_MESSAGE(10),
    /**
     * Represents a project inserted notification. Associated with the integer value 11.
     */
    PROJECT_INSERTED(11),
    /**
     * Represents a user type changed notification. Associated with the integer value 12.
     */
    USER_TYPE_CHANGED(12),
    /**
     * Represents a project updated notification. Associated with the integer value 13.
     */
    PROJECT_UPDATED(13),
    /**
     * Represents a task assigned notification. Associated with the integer value 14.
     */
    TASK_ASSIGNED(14),
    /**
     * Represents a left project notification. Associated with the integer value 15.
     */
    LEFT_PROJECT(15),
    /**
     * Represents a promote to admin notification. Associated with the integer value 16.
     */
    PROMOTE_TO_ADMIN(16);
    /**
     * The integer value associated with the enum constant.
     */
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
