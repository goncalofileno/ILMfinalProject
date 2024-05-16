package com.ilm.projecto_ilm_backend.ENUMS;

/**
 * Enum representing different states of a project.
 */
public enum StateProjectENUM {
    /**
     * Planning state.
     * Represented by the integer value 0.
     */
    PLANING(0),

    /**
     * Ready state.
     * Represented by the integer value 1.
     */
    READY(1),

    /**
     * Submitted state.
     * Represented by the integer value 2.
     */
    SUBMITTED(2),

    /**
     * Approved state.
     * Represented by the integer value 3.
     */
    APPROVED(3),

    /**
     * In-progress state.
     * Represented by the integer value 4.
     */
    IN_PROGRESS(4),

    /**
     * Canceled state.
     * Represented by the integer value 5.
     */
    CANCELED(5),

    /**
     * Finished state.
     * Represented by the integer value 6.
     */
    FINISHED(6);

    private final int intValue;

    /**
     * Constructor for StateProjectENUM.
     * Associates an integer value with the enum constant.
     *
     * @param intValue the integer value associated with the enum constant
     */
    StateProjectENUM(int intValue) {
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
     * Converts an integer value to the corresponding StateProjectENUM constant.
     *
     * @param intValue the integer value to convert
     * @return the corresponding StateProjectENUM constant
     * @throws IllegalArgumentException if the integer value does not correspond to any StateProjectENUM constant
     */
    public static StateProjectENUM fromInt(int intValue) {
        for (StateProjectENUM state : StateProjectENUM.values()) {
            if (state.getIntValue() == intValue) {
                return state;
            }
        }
        throw new IllegalArgumentException("Invalid integer value for StateProjectENUM: " + intValue);
    }
}

