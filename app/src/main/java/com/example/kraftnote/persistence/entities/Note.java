package com.example.kraftnote.persistence.entities;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

@Entity(tableName = "Notes", foreignKeys = {
        @ForeignKey(entity = Category.class, parentColumns = "id",
                childColumns = "category_id", onDelete = ForeignKey.RESTRICT)
})
public class Note extends BaseEntity {
    private String name;

    @ColumnInfo(name = "category_id")
    private int categoryId;

    @ColumnInfo(defaultValue = "0")
    private int archived;

    @ColumnInfo(defaultValue = "")
    private String body;

    @Ignore
    public Note() {}

    public Note(String name, String body, int categoryId) {
        this.name = name;
        this.body = body;
        this.categoryId = categoryId;
        this.archived = 0;
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
}
