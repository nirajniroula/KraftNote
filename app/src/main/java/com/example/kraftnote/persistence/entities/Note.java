package com.example.kraftnote.persistence.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.kraftnote.persistence.converters.CreatedAtConverter;

import java.util.Date;

@Entity(tableName = "Notes", foreignKeys = {@ForeignKey(entity = Category.class, parentColumns = "category_id",
        childColumns = "note_category_id", onDelete = ForeignKey.RESTRICT)}
)
public class Note {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    protected Integer id;

    @ColumnInfo(name = "note_name")
    private String name;

    @ColumnInfo(name = "note_category_id", index = true)
    private int categoryId;

    @ColumnInfo(name = "note_archived", defaultValue = "0")
    private int archived;

    @ColumnInfo(name = "note_body", defaultValue = "")
    private String body;

    @TypeConverters(CreatedAtConverter.class)
    @ColumnInfo(name = "note_created_at", defaultValue = "CURRENT_TIMESTAMP")
    protected Date createdAt;

    @Ignore
    public Note() {
    }

    public Note(Integer id, String name, int categoryId, int archived, String body, Date createdAt) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.archived = archived;
        this.body = body;
        setCreatedAt(createdAt);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getBody() {
        return body;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getArchived() {
        return archived;
    }

    public void setArchived(int archived) {
        this.archived = archived;
    }

    @Ignore
    public boolean isArchived() {
        return archived == 0;
    }

    public String getName() {
        return name;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = (createdAt == null) ? new Date() : createdAt;
    }
}
