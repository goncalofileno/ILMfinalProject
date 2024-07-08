package com.ilm.projecto_ilm_backend.ENUMS;

public enum LanguageENUM {

    ENGLISH(1),
    PORTUGUESE(2);

    private final int intValue;

    LanguageENUM(int intValue) {
        this.intValue = intValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public static LanguageENUM fromInt(int i) {
        for (LanguageENUM type : LanguageENUM.values()) {
            if (type.getIntValue() == i) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid integer value for LanguageENUM: " + i);
    }
}
