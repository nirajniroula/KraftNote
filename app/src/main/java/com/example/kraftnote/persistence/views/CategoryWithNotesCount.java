package com.example.kraftnote.persistence.views;

import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;
import androidx.room.Embedded;

import com.example.kraftnote.persistence.entities.Category;

@DatabaseView("SELECT Categories.*, COUNT(Notes.id) as notes_count FROM Categories " +
        "LEFT JOIN Notes ON Notes.category_id = Categories.id GROUP BY Categories.id")
public class CategoryWithNotesCount {
    @Embedded
    public Category category;

    @ColumnInfo(name = "notes_count")
    public int notesCount;
}
