package com.example.kraftnote.persistence.converters;

import androidx.room.TypeConverter;

import com.example.kraftnote.persistence.entities.NoteFile;

public class FileTypeConverter {
    @TypeConverter
    public static NoteFile.FileType toType(String value) {
        return NoteFile.FileType.get(value);
    }

    @TypeConverter
    public static String toValue(NoteFile.FileType type) {
        return type.getValue();
    }
}
