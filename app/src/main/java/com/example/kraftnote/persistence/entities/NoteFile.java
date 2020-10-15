package com.example.kraftnote.persistence.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.TypeConverters;

import com.example.kraftnote.persistence.converters.FileTypeConverter;

import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "NoteFiles", foreignKeys = {
        @ForeignKey(entity = Note.class, parentColumns = "id",
                childColumns = "note_id", onDelete = ForeignKey.CASCADE)
})
public class NoteFile extends BaseEntity {
    public enum FileType {
        IMAGE("image"),
        AUDIO("audio");

        private static Map<String, FileType> map = new HashMap<>();
        private String value;

        FileType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static FileType get(String value) {
            return map.get(value);
        }

        static {
            for (FileType type : values()) {
                map.put(type.getValue(), type);
            }
        }
    }


    private String location;

    @TypeConverters(FileTypeConverter.class)
    private FileType type;

    @ColumnInfo(name = "note_id")
    private Integer noteId;

    @Ignore
    public NoteFile() {
    }

    public NoteFile(String location, FileType type, Integer noteId) {
        this.location = location;
        this.type = type;
        this.noteId = noteId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }
}
