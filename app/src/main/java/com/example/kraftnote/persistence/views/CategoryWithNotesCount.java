package com.example.kraftnote.persistence.views;

import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;
import androidx.room.Embedded;
import androidx.room.Ignore;

import com.example.kraftnote.persistence.entities.Category;

@DatabaseView("SELECT Categories.*, COUNT(Notes.note_id) as notes_count FROM Categories " +
        "LEFT JOIN Notes ON Notes.note_category_id = Categories.category_id GROUP BY Categories.category_id")
public class CategoryWithNotesCount {
    @Embedded
    private Category category;

    @ColumnInfo(name = "notes_count")
    private int notesCount;

    public CategoryWithNotesCount(Category category, int notesCount) {
        this.category = category;
        this.notesCount = notesCount;
    }

    @Ignore
    public Integer getId() {
        return category.getId();
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getNotesCount() {
        return notesCount;
    }

    public void setNotesCount(int notesCount) {
        this.notesCount = notesCount;
    }
}
