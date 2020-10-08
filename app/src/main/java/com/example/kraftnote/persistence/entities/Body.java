package com.example.kraftnote.persistence.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.TypeConverters;

import com.example.kraftnote.persistence.converters.BodyTypeConverter;
import com.example.kraftnote.persistence.entities.contracts.BodyType;
import com.example.kraftnote.persistence.entities.contracts.IBody;


@Entity(tableName = "NoteBodies", foreignKeys = {
        @ForeignKey(entity = Note.class, parentColumns = "id",
                childColumns = "note_id", onDelete = ForeignKey.CASCADE)
})
public class Body extends BaseEntity implements IBody {
    private String content;

    @ColumnInfo(name = "type")
    @TypeConverters(BodyTypeConverter.class)
    private BodyType type;

    @ColumnInfo(name = "note_id")
    private int noteId;

    private int priority;

    public Body(String content, int noteId, int priority) {
        this.content = content;
        this.noteId = noteId;
        this.priority = priority;
    }

    @Override
    public BodyType getType() {
        return type;
    }

    @Override
    public void setType(BodyType type) {
        this.type = type;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int getNoteId() {
        return noteId;
    }

    @Override
    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }
}
