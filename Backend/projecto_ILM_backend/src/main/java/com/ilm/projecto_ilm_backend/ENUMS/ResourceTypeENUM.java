package com.ilm.projecto_ilm_backend.ENUMS;

/**
 * Enum representing different types of resources.
 */
public enum ResourceTypeENUM {
    /**
     * General resource type.
     * Represented by the integer value 0.
     */
    RESOURCE(0),

    /**
     * Component type.
     * Represented by the integer value 1.
     */
    COMPONENT(1);

    private final int intValue;

    /**
     * Constructor for ResourceTypeENUM.
     * Associates an integer value with the enum constant.
     *
     * @param intValue the integer value associated with the enum constant
     */
    ResourceTypeENUM(int intValue) {
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
     * Converts an integer value to the corresponding ResourceTypeENUM constant.
     *
     * @param intValue the integer value to convert
     * @return the corresponding ResourceTypeENUM constant
     * @throws IllegalArgumentException if the integer value does not correspond to any ResourceTypeENUM constant
     */
    public static ResourceTypeENUM fromInt(int intValue) {
        for (ResourceTypeENUM type : ResourceTypeENUM.values()) {
            if (type.getIntValue() == intValue) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid integer value for ResourceTypeENUM: " + intValue);
    }
}

