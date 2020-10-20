package com.example.kraftnote.persistence.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.kraftnote.persistence.converters.CreatedAtConverter;

import java.util.Date;

@Entity(tableName = "Todos", foreignKeys = {
        @ForeignKey(entity = Note.class, parentColumns = "note_id",
                childColumns = "todo_note_id", onDelete = ForeignKey.CASCADE)
})
public class Todo {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "todo_id")
    protected Integer id;

    @ColumnInfo(name = "todo_task")
    private String task;

    @ColumnInfo(defaultValue = "0", name = "todo_completed")
    private Integer completed;

    @ColumnInfo(name = "todo_note_id", index = true)
    private Integer noteId;

    @TypeConverters(CreatedAtConverter.class)
    @ColumnInfo(name = "todo_created_at", defaultValue = "CURRENT_TIMESTAMP")
    protected Date createdAt;

    @Ignore
    public Todo() {
    }

    @Ignore
    public Todo(String task, Integer noteId) {
        setTask(task);
        setNoteId(noteId);
        setCreatedAt(null);
    }

    public Todo(Integer id, String task, Integer completed, Integer noteId, Date createdAt) {
        this.id = id;
        this.task = task;
        this.completed = completed;
        this.noteId = noteId;
        setCreatedAt(createdAt);
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Integer getCompleted() {
        return completed;
    }

    public void setCompleted(Integer completed) {
        this.completed = completed == null ? 0 : 1;
    }

    @Ignore
    public void setCompleted(boolean completed) {
        this.completed = completed ? 1 : 0;
    }

    @Ignore
    public boolean isCompleted() {
        return completed != null && completed == 1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
