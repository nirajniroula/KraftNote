package com.example.kraftnote.persistence.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.kraftnote.persistence.entities.Category;
import com.example.kraftnote.persistence.views.CategoryWithNotesCount;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    long insertAll(Category category);

    @Insert
    void insertAll(Category... categories);

    @Update
    void updateAll(Category... categories);

    @Delete
    void deleteAll(Category... categories);

    @Query("SELECT * FROM Categories WHERE category_hidden = 0 ORDER BY category_created_at")
    LiveData<List<Category>> getAll();

    @Query("SELECT * FROM Categories WHERE category_id=:id LIMIT 1")
    Category findById(int id);

    @Query("SELECT * FROM CategoryWithNotesCount WHERE category_hidden = 0")
    LiveData<List<CategoryWithNotesCount>> getAllWithNotesCount();

    @Query("SELECT EXISTS(SELECT * FROM Categories WHERE LOWER(category_name) = LOWER(:name) LIMIT 1)")
    boolean nameExists(String name);

    @Insert
    long insertSingle(Category category);
}
