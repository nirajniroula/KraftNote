package com.example.kraftnote.persistence.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.kraftnote.persistence.KraftNoteDatabase;
import com.example.kraftnote.persistence.daos.CategoryDao;
import com.example.kraftnote.persistence.daos.TodoDao;
import com.example.kraftnote.persistence.entities.Category;
import com.example.kraftnote.persistence.entities.Todo;
import com.example.kraftnote.persistence.repositories.contracts.IRepository;

import java.util.List;

public class TodoRepository implements IRepository<Todo> {
    private TodoDao todoDao;
    private LiveData<List<Todo>> todoLiveData;

    public TodoRepository(Application application) {
        KraftNoteDatabase database = KraftNoteDatabase.getInstance(application);
        todoDao = database.todoDao();
        todoLiveData = todoDao.getAll();
    }

    @Override
    public void insert(Todo todo) {
        todoDao.insertAll(todo);
    }

    @Override
    public void update(Todo todo) {
        todoDao.updateAll(todo);
    }

    @Override
    public void delete(Todo todo) {
        todoDao.deleteAll(todo);
    }

    @Override
    public LiveData<List<Todo>> getAll() {
        return todoLiveData;
    }
}
