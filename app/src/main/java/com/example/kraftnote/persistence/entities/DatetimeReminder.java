package com.example.kraftnote.persistence.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.kraftnote.persistence.converters.CreatedAtConverter;
import com.example.kraftnote.persistence.converters.DateConverter;

import java.util.Date;

@Entity(tableName = "DatetimeReminders", foreignKeys = {
        @ForeignKey(entity = Note.class, parentColumns = "note_id",
                childColumns = "datetime_reminder_note_id", onDelete = ForeignKey.CASCADE)
})
public class DatetimeReminder {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "datetime_reminder_id")
    protected Integer id;

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "datetime_reminder_datetime")
    private Date datetime;

    @ColumnInfo(name = "datetime_reminder_note_id", index = true)
    private Integer noteId;

    @TypeConverters(CreatedAtConverter.class)
    @ColumnInfo(name = "datetime_reminder_created_at", defaultValue = "CURRENT_TIMESTAMP")
    protected Date createdAt;

    @Ignore
    public DatetimeReminder() {
    }

    @Ignore
    public DatetimeReminder(Date datetime) {
        this.datetime = datetime;
    }

    public DatetimeReminder(Integer id, Date datetime, Integer noteId, Date createdAt) {
        this.id = id;
        this.datetime = datetime;
        this.noteId = noteId;
        setCreatedAt(createdAt);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = (createdAt == null) ? new Date() : createdAt;
    }
}
