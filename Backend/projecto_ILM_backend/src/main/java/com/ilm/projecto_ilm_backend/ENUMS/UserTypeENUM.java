package com.ilm.projecto_ilm_backend.ENUMS;

/**
 * Enum representing different types of users.
 */
public enum UserTypeENUM {
    /**
     * Admin user type.
     * Represented by the integer value 0.
     */
    ADMIN(0),

    /**
     * Standard user type.
     * Represented by the integer value 1.
     */
    STANDARD_USER(1),

    /**
     * Guest user type.
     * Represented by the integer value 2.
     */
    GUEST(2);

    private final int intValue;

    /**
     * Constructor for UserTypeENUM.
     * Associates an integer value with the enum constant.
     *
     * @param intValue the integer value associated with the enum constant
     */
    UserTypeENUM(int intValue) {
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
     * Converts an integer value to the corresponding UserTypeENUM constant.
     *
     * @param intValue the integer value to convert
     * @return the corresponding UserTypeENUM constant
     * @throws IllegalArgumentException if the integer value does not correspond to any UserTypeENUM constant
     */
    public static UserTypeENUM fromInt(int intValue) {
        for (UserTypeENUM type : UserTypeENUM.values()) {
            if (type.getIntValue() == intValue) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid integer value for UserTypeENUM: " + intValue);
    }
}

