package com.ilm.projecto_ilm_backend.ENUMS;

/**
 * Enum representing different types of user roles in a project.
 */
public enum UserInProjectTypeENUM {

    CREATOR(0),

    MANAGER(1),

    MEMBER(2),

    MEMBER_BY_INVITATION(3),

    MEMBER_BY_APPLIANCE(4),

    PENDING_BY_APPLIANCE(5),

    PENDING_BY_INVITATION(6),

    EXMEMBER(7);

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

