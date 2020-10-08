package com.example.kraftnote.persistence.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.kraftnote.persistence.entities.Tag;

import java.util.List;

@Dao
public interface TagDao {
    @Insert
    void insertAll(Tag... tags);

    @Update
    void updateAll(Tag... tags);

    @Delete
    void deleteAll(Tag... tags);

    @Query("SELECT * FROM Tags ORDER BY name")
    LiveData<List<Tag>> getAll();

    @Query("SELECT * FROM Tags WHERE name LIKE :name ORDER BY name")
    LiveData<List<Tag>> searchByName(String name);
}
