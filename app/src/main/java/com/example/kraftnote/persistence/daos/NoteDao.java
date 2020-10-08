package com.example.kraftnote.persistence.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.kraftnote.persistence.entities.Note;
import com.example.kraftnote.persistence.relations.NoteWithBodies;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    void insertAll(Note... notes);

    @Update
    void updateAll(Note... notes);

    @Delete
    void deleteAll(Note... notes);

    @Query("SELECT * FROM Notes ORDER BY created_at DESC")
    LiveData<List<Note>> getAll();
}
