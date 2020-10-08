package com.example.kraftnote.persistence.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.kraftnote.persistence.entities.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insertAll(Category... categories);

    @Update
    void updateAll(Category... categories);

    @Delete
    void deleteAll(Category... categories);

    @Query("SELECT * FROM Categories ORDER BY created_at")
    LiveData<List<Category>> getAll();
}
