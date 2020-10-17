package com.example.kraftnote.persistence.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.kraftnote.persistence.entities.NoteFile;

import java.util.List;

@Dao
public interface NoteFileDao {
    @Insert
    void insertAll(NoteFile... noteFiles);

    @Update
    void updateAll(NoteFile... noteFiles);

    @Delete
    void deleteAll(NoteFile... noteFiles);

    @Query("SELECT * FROM NoteFiles ORDER BY note_file_created_at DESC")
    LiveData<List<NoteFile>> getAll();

    @Query("SELECT * FROM NoteFiles WHERE note_file_note_id = :id ORDER BY note_file_created_at DESC")
    LiveData<List<NoteFile>> getAllForNote(int id);
}
