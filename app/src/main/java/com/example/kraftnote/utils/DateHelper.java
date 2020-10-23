package com.example.kraftnote.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateHelper {
    public static boolean isFuture(Date date) {
        return date.getTime() - new Date().getTime() > 0;
    }

    public static boolean isPast(Date date) {
        return !isFuture(date);
    }

    public static Date toDate(long ts) {
        return new Date(ts);
    }

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

    public static String toFormattedStringAlt(LocalDateTime localDateTime) {
        return toFormattedString(localDateTime, "EEE, d MMM yyyy, hh:mm a");
    }

    public static String toFormattedStringAlt(Date date) {
        return toFormattedStringAlt(toLocalDateTime(date));
    }

    public static String toFormattedString(LocalDateTime localDateTime, String patten) {
        return localDateTime.format(DateTimeFormatter.ofPattern(patten));
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
