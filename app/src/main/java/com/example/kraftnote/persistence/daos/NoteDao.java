package com.example.kraftnote.persistence.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.kraftnote.persistence.entities.Note;
import com.example.kraftnote.persistence.views.NoteWithRelation;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    void insertAll(Note... notes);

    @Update
    void updateAll(Note... notes);

    @Delete
    void deleteAll(Note... notes);

    @Query("SELECT * FROM Notes WHERE note_is_draft = 0 ORDER BY note_created_at DESC")
    LiveData<List<Note>> getAll();

    @Query("SELECT * FROM Notes WHERE note_is_draft = :draft ORDER BY note_created_at DESC")
    LiveData<List<Note>> getAll(int draft);

    @Transaction
    @Query("SELECT * FROM NoteWithRelation WHERE note_is_draft = 0 ORDER BY note_created_at DESC")
    LiveData<List<NoteWithRelation>> getAllWithRelation();

    @Transaction
    @Query("SELECT * FROM NoteWithRelation WHERE note_id=:id AND note_is_draft = 0 ORDER BY note_created_at DESC")
    LiveData<List<NoteWithRelation>> getAllWithRelationFor(int id);

    @Query("SELECT * FROM Notes WHERE note_id=:id LIMIT 1")
    Note findById(int id);

    @Query("SELECT * FROM Notes WHERE note_is_draft=1 ORDER BY note_created_at DESC LIMIT 1")
    Note getLatestDraft();

    @Insert
    long insertSingle(Note note);
}
