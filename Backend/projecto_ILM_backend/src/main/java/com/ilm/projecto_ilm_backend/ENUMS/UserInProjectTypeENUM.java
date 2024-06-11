package com.ilm.projecto_ilm_backend.ENUMS;

/**
 * Enum representing different types of user roles in a project.
 */
public enum UserInProjectTypeENUM {
    /**
     * Creator role.
     * Represented by the integer value 0.
     */
    CREATOR(0),

    /**
     * Manager role.
     * Represented by the integer value 1.
     */
    MANAGER(1),

    /**
     * Member role.
     * Represented by the integer value 2.
     */
    MEMBER(2),

    /**
     * Pending role.
     * Represented by the integer value 3.
     */
    PENDING(3),

    /**
     * Exmember role.
     * Represented by the integer value 4.
     */
    EXMEMBER(4),

    /**
     * Invited role.
     * Represented by the integer value 5.
     */
    INVITED(5);


    private final int intValue;

    /**
     * Constructor for UserInProjectTypeENUM.
     * Associates an integer value with the enum constant.
     *
     * @param intValue the integer value associated with the enum constant
     */
    UserInProjectTypeENUM(int intValue) {
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
     * Converts an integer value to the corresponding UserInProjectTypeENUM constant.
     *
     * @param intValue the integer value to convert
     * @return the corresponding UserInProjectTypeENUM constant
     * @throws IllegalArgumentException if the integer value does not correspond to any UserInProjectTypeENUM constant
     */
    public static UserInProjectTypeENUM fromInt(int intValue) {
        for (UserInProjectTypeENUM role : UserInProjectTypeENUM.values()) {
            if (role.getIntValue() == intValue) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid integer value for UserInProjectTypeENUM: " + intValue);
    }
}

