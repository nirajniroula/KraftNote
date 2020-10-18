package com.example.kraftnote.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateHelper {
    public static Date toDate(LocalDateTime localDateTime) {
        return java.sql.Date.from(localDateTime.toInstant(OffsetDateTime.now().getOffset()));
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime timestampToLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()
        );
    }

    public static Date timestampToDate(long timestamp, int hour, int minute) {
        return toDate(
                timestampToLocalDateTime(timestamp).withHour(hour).withMinute(minute)
        );
    }

    public static String toFormattedString(LocalDateTime localDateTime) {
        return toFormattedString(localDateTime, "EEEE, d MMMM yyyy hh:mm a");
    }

    public static String toFormattedString(LocalDateTime localDateTime, String patten) {
        return localDateTime.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy hh:mm a"));
    }

    public static String toFileNameFormat() {
        return toFormattedString(LocalDateTime.now(), "yyyyMMdd_HHmmss");
    }

    public static String toFileNameFormat(String prefix) {
        return prefix + toFileNameFormat();
    }

    public static String toFileNameFormat(String prefix, String suffix) {
        return prefix + toFileNameFormat() + suffix;
    }

    public static String toFormattedString(Date date) {
        return toFormattedString(toLocalDateTime(date));
    }

    public static int getCurrentHour() {
        return LocalDateTime.now().getHour();
    }

    public static int getCurrentMinute() {
        return LocalDateTime.now().getMinute();
    }
}
