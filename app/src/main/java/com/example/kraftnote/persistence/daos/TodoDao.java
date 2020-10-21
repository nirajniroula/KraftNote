package com.example.kraftnote.persistence.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.kraftnote.persistence.entities.Todo;

import java.util.List;

@Dao
public interface TodoDao {
    @Insert
    long insertSingle(Todo todo);

    @Insert
    long[] insert(Todo... todos);

    @Update
    void updateAll(Todo... todos);

    @Delete
    void deleteAll(Todo... todos);

    @Query("SELECT * FROM Todos ORDER BY todo_created_at ASC")
    LiveData<List<Todo>> getAll();

    @Query("SELECT * FROM Todos WHERE todo_task LIKE :name ORDER BY todo_task ASC")
    LiveData<List<Todo>> searchByName(String name);
}
