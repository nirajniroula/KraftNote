package com.example.kraftnote.persistence.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.kraftnote.persistence.entities.Category;
import com.example.kraftnote.persistence.repositories.CategoryRepository;
import com.example.kraftnote.persistence.viewmodels.contracts.IViewModel;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel implements IViewModel<Category> {
    private final CategoryRepository categoryRepository;
    private LiveData<List<Category>> allCategories;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
        allCategories = categoryRepository.getAll();
    }

    @Override
    public void insert(Category category) {
        categoryRepository.insert(category);
    }

    @Override
    public void update(Category category) {
        categoryRepository.update(category);
    }

    @Override
    public void delete(Category category) {
        categoryRepository.delete(category);
    }

    @Override
    public LiveData<List<Category>> getAll() {
        return allCategories;
    }
}
