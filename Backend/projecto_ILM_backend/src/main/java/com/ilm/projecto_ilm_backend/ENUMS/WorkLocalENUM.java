package com.ilm.projecto_ilm_backend.ENUMS;

/**
 * Enum representing different work locations.
 */
public enum WorkLocalENUM {
    /**
     * Lisbon work location.
     * Represented by the integer value 0.
     */
    LISBON(0),

    /**
     * Porto work location.
     * Represented by the integer value 1.
     */
    PORTO(1),

    /**
     * Coimbra work location.
     * Represented by the integer value 2.
     */
    COIMBRA(2),

    /**
     * Tomar work location.
     * Represented by the integer value 3.
     */
    TOMAR(3),

    /**
     * Viseu work location.
     * Represented by the integer value 4.
     */
    VISEU(4),

    /**
     * Vila Real work location.
     * Represented by the integer value 5.
     */
    VILA_REAL(5);

    private final int intValue;

    //Metodo que recebe uma string e verifica se é um dos valores possiveis do ENUM
    public static boolean contains(String test) {

        for (WorkLocalENUM c : WorkLocalENUM.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Constructor for WorkLocalENUM.
     * Associates an integer value with the enum constant.
     *
     * @param intValue the integer value associated with the enum constant
     */
    WorkLocalENUM(int intValue) {
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
     * Converts an integer value to the corresponding WorkLocalENUM constant.
     *
     * @param intValue the integer value to convert
     * @return the corresponding WorkLocalENUM constant
     * @throws IllegalArgumentException if the integer value does not correspond to any WorkLocalENUM constant
     */
    public static WorkLocalENUM fromInt(int intValue) {
        for (WorkLocalENUM location : WorkLocalENUM.values()) {
            if (location.getIntValue() == intValue) {
                return location;
            }
        }
        throw new IllegalArgumentException("Invalid integer value for WorkLocalENUM: " + intValue);
    }
}

