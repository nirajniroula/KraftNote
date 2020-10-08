package com.example.kraftnote.persistence.converters;

import androidx.room.TypeConverter;

import com.example.kraftnote.persistence.entities.contracts.BodyType;

public class BodyTypeConverter {
    @TypeConverter
    public static BodyType toType(String key) {
        BodyType type = BodyType.make(key);

        if (type == null)
            throw new IllegalArgumentException("Illegal body type - " + key);

        return type;
    }

    @TypeConverter
    public static String toString(BodyType type) {
        return type.getValue();
    }
}
