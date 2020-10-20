package com.example.kraftnote.persistence.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.kraftnote.persistence.entities.Todo;
import com.example.kraftnote.persistence.repositories.TodoRepository;
import com.example.kraftnote.persistence.viewmodels.contracts.IViewModel;

import java.util.List;

public class TodoViewModel extends AndroidViewModel implements IViewModel<Todo> {
    private TodoRepository repository;
    private LiveData<List<Todo>> todoLiveData;

    public TodoViewModel(@NonNull Application application) {
        super(application);
        repository = new TodoRepository(application);
        todoLiveData = repository.getAll();
    }

    @Override
    public void insert(Todo todo) {
        repository.insert(todo);
    }

    @Override
    public void update(Todo todo) {
        repository.update(todo);
    }

    @Override
    public void delete(Todo todo) {
        repository.delete(todo);
    }

    @Override
    public LiveData<List<Todo>> getAll() {
        return todoLiveData;
    }
}
