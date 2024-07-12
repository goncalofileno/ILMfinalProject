package com.ilm.projecto_ilm_backend.ENUMS;

/**
 * Enum representing different types of user roles in a project.
 */
public enum UserInProjectTypeENUM {

    /**
     * Represents the creator of the project. Associated with the integer value 0.
     */
    CREATOR(0),
    /**
     * Represents a manager in the project. Associated with the integer value 1.
     */
    MANAGER(1),
    /**
     * Represents a member in the project. Associated with the integer value 2.
     */
    MEMBER(2),
    /**
     * Represents a member who joined the project by invitation. Associated with the integer value 3.
     */
    MEMBER_BY_INVITATION(3),
    /**
     * Represents a member who joined the project by appliance. Associated with the integer value 4.
     */
    MEMBER_BY_APPLIANCE(4),
    /**
     * Represents a user who is pending to join the project by appliance. Associated with the integer value 5.
     */
    PENDING_BY_APPLIANCE(5),
    /**
     * Represents a user who is pending to join the project by invitation. Associated with the integer value 6.
     */
    PENDING_BY_INVITATION(6),
    /**
     * Represents a user who was a member of the project but is no longer. Associated with the integer value 7.
     */
    EXMEMBER(7),
    /**
     * Represents an admin of the project. Associated with the integer value 8.
     */
    ADMIN(8),
    /**
     * Represents a guest in the project. Associated with the integer value 9.
     */
    GUEST(9);
    /**
     * The integer value associated with the enum constant.
     */
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

