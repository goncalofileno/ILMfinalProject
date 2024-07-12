package com.ilm.projecto_ilm_backend.ENUMS;
/**
 * Enum representing different languages.
 * Each language is associated with a unique integer value.
 */
public enum LanguageENUM {
    /**
     * Represents English language. Associated with the integer value 1.
     */
    ENGLISH(1),
    /**
     * Represents Portuguese language. Associated with the integer value 2.
     */
    PORTUGUESE(2);
    /**
     * The integer value associated with the enum constant.
     */
    private final int intValue;
    /**
     * Constructor for LanguageENUM.
     * Associates an integer value with the enum constant.
     *
     * @param intValue the integer value associated with the enum constant
     */
    LanguageENUM(int intValue) {
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
     * Converts an integer value to the corresponding LanguageENUM constant.
     *
     * @param i the integer value to convert
     * @return the corresponding LanguageENUM constant
     * @throws IllegalArgumentException if the integer value does not correspond to any LanguageENUM constant
     */
    public static LanguageENUM fromInt(int i) {
        for (LanguageENUM type : LanguageENUM.values()) {
            if (type.getIntValue() == i) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid integer value for LanguageENUM: " + i);
    }
}
