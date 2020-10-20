package com.example.kraftnote.persistence.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.kraftnote.persistence.converters.CreatedAtConverter;
import com.example.kraftnote.persistence.converters.FileTypeConverter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "NoteFiles", foreignKeys = {
        @ForeignKey(entity = Note.class, parentColumns = "note_id",
                childColumns = "note_file_note_id", onDelete = ForeignKey.CASCADE)
})
public class NoteFile {
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

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_file_id")
    protected Integer id;

    @ColumnInfo(name = "note_file_location")
    private String location;

    @TypeConverters(FileTypeConverter.class)
    @ColumnInfo(name = "note_file_type", index = true)
    private FileType type;

    @ColumnInfo(name = "note_file_note_id", index = true)
    private Integer noteId;

    @TypeConverters(CreatedAtConverter.class)
    @ColumnInfo(name = "note_file_created_at", defaultValue = "CURRENT_TIMESTAMP")
    protected Date createdAt;

    @Ignore
    public NoteFile() {
    }

    public NoteFile(Integer id, String location, FileType type, Integer noteId, Date createdAt) {
        this.id = id;
        this.location = location;
        this.type = type;
        this.noteId = noteId;
        setCreatedAt(createdAt);
    }

    @Ignore
    public static NoteFile newImage(String location, Integer noteId) {
        NoteFile noteFile = new NoteFile();

        noteFile.setType(FileType.IMAGE);
        noteFile.setLocation(location);
        noteFile.setCreatedAt(null);
        noteFile.setNoteId(noteId);

        return noteFile;
    }

    public static NoteFile newAudio(String location, Integer noteId) {
        NoteFile noteFile = new NoteFile();

        noteFile.setType(FileType.AUDIO);
        noteFile.setLocation(location);
        noteFile.setCreatedAt(null);
        noteFile.setNoteId(noteId);

        return noteFile;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Ignore
    public boolean isImage() {
        return getType() == FileType.IMAGE;
    }

    @Ignore
    public boolean isAudio() {
        return getType() == FileType.AUDIO;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = (createdAt == null) ? new Date() : createdAt;
    }

    @Override
    public String toString() {
        return "NoteFile{" +
                "id=" + id +
                ", location='" + location + '\'' +
                ", type=" + type +
                ", noteId=" + noteId +
                ", createdAt=" + createdAt +
                '}';
    }
}
