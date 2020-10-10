package com.example.kraftnote.persistence.views;

import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;
import androidx.room.Embedded;

import com.example.kraftnote.persistence.entities.Category;

@DatabaseView("SELECT Categories.*, COUNT(Notes.id) as notes_count FROM Categories " +
        "LEFT JOIN Notes ON Notes.category_id = Categories.id GROUP BY Categories.id")
public class CategoryWithNotesCount {
    @Embedded
    private Category category;

    @ColumnInfo(name = "notes_count")
    private int notesCount;

    public CategoryWithNotesCount(Category category, int notesCount) {
        this.category = category;
        this.notesCount = notesCount;
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
