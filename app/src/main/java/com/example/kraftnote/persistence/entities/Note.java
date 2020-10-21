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
    private Integer categoryId;

    @ColumnInfo(name = "note_archived", defaultValue = "0")
    private int archived;

    @ColumnInfo(name = "note_body", defaultValue = "")
    private String body;

    @ColumnInfo(name = "note_is_draft", defaultValue = "0")
    private int draft;

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

    public static Note newDraft() {
        Note note = new Note();
        note.draft = 1;
        note.categoryId = 1;
        note.setCreatedAt(null);

        return note;
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

    public Integer getCategoryId() {
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
        return archived == 1;
    }

    @Ignore
    public boolean isDraft() {
        return draft == 1;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public void setArchived(Integer archived) {
        this.archived = archived;
    }

    public Integer getDraft() {
        return draft;
    }

    public void setDraft(Integer draft) {
        this.draft = draft;
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

    @Ignore
    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categoryId=" + categoryId +
                ", archived=" + archived +
                ", body='" + body + '\'' +
                ", draft=" + draft +
                ", createdAt=" + createdAt +
                '}';
    }
}
