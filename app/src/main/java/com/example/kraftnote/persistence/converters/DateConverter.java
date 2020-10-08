package com.example.kraftnote.persistence.converters;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date.getTime();
    }

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return new Date(timestamp);
    }

}
