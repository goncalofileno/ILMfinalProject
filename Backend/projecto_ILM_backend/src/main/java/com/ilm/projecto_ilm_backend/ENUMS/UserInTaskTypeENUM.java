package com.ilm.projecto_ilm_backend.ENUMS;

/**
 * Enum representing different types of user roles in a task.
 */
public enum UserInTaskTypeENUM {

    /**
     * Creator role.
     * Represented by the integer value 0.
     */
    CREATOR(0),

    /**
     * Member role.
     * Represented by the integer value 1.
     */
    MEMBER(1),

    /**
     * Creator in charge role.
     * Represented by the integer value 2.
     */
    CREATOR_INCHARGE(2),

    /**
     * Member in charge role.
     * Represented by the integer value 3.
     */
    INCHARGE(3);

    private final int intValue;

    /**
     * Constructor for UserInTaskTypeENUM.
     * Associates an integer value with the enum constant.
     *
     * @param intValue the integer value associated with the enum constant
     */
    UserInTaskTypeENUM(int intValue) {
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
     * Converts an integer value to the corresponding UserInTaskTypeENUM constant.
     *
     * @param intValue the integer value to convert
     * @return the corresponding UserInTaskTypeENUM constant
     * @throws IllegalArgumentException if the integer value does not correspond to any UserInTaskTypeENUM constant
     */
    public static UserInTaskTypeENUM fromInt(int intValue) {
        for (UserInTaskTypeENUM role : UserInTaskTypeENUM.values()) {
            if (role.getIntValue() == intValue) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid integer value for UserInTaskTypeENUM: " + intValue);
    }
}

