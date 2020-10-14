package com.example.kraftnote.persistence.views;

import androidx.room.DatabaseView;

//@DatabaseView("SELECT Categories.*, Notes.* FROM NOTES " +
//        "JOIN Categories ON Notes.category_id = Categories.id WHERE Notes.id = :id")
public class NoteWithCategory {
    public int id;
}
