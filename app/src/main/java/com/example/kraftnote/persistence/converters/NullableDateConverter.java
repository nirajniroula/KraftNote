package com.example.kraftnote.persistence.converters;

import androidx.room.TypeConverter;

import java.util.Date;

public class NullableDateConverter {
    @TypeConverter
    public static Long toTimestamp(Date date) {
        if (date == null) return null;

        return date.getTime();
    }

    @TypeConverter
    public static Date toDate(Long timestamp) {
        if (timestamp == null) return null;

        return new Date(timestamp);
    }
}
