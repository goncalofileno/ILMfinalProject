package com.ilm.projecto_ilm_backend.ENUMS;

/**
 * Enum representing different types of skills.
 */
public enum SkillTypeENUM {
    /**
     * Knowledge-based skill.
     * Represented by the integer value 0.
     */
    KNOWLEDGE(0),

    /**
     * Software skill.
     * Represented by the integer value 1.
     */
    SOFTWARE(1),

    /**
     * Hardware skill.
     * Represented by the integer value 2.
     */
    HARDWARE(2),

    /**
     * Tools skill.
     * Represented by the integer value 3.
     */
    TOOLS(3);

    private final int intValue;

    /**
     * Constructor for SkillTypeENUM.
     * Associates an integer value with the enum constant.
     *
     * @param intValue the integer value associated with the enum constant
     */
    SkillTypeENUM(int intValue) {
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
     * Converts an integer value to the corresponding SkillTypeENUM constant.
     *
     * @param intValue the integer value to convert
     * @return the corresponding SkillTypeENUM constant
     * @throws IllegalArgumentException if the integer value does not correspond to any SkillTypeENUM constant
     */
    public static SkillTypeENUM fromInt(int intValue) {
        for (SkillTypeENUM type : SkillTypeENUM.values()) {
            if (type.getIntValue() == intValue) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid integer value for SkillTypeENUM: " + intValue);
    }
}
