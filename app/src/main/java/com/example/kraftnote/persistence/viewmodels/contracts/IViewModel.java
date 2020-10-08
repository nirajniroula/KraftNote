package com.example.kraftnote.persistence.viewmodels.contracts;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface IViewModel<T> {
    void insert(T t);
    void update(T t);
    void delete(T t);
    LiveData<List<T>> getAll();
}
