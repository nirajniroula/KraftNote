package com.example.kraftnote.persistence.entities.contracts;

import java.util.HashMap;

public enum BodyType {
    Heading("H"),
    Paragraph("P"),
    Audio("A"),
    Image("I");

    private String value;
    private static final HashMap<String, BodyType> CACHE = new HashMap<>();

    BodyType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static BodyType make(String key) {
        return CACHE.get(key);
    }

    static  {
        for (BodyType type: values()) {
            BodyType.CACHE.put(type.getValue(), type);
        }
    }
}
