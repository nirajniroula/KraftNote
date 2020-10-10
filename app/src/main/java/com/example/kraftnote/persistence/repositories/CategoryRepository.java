package com.example.kraftnote.persistence.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.kraftnote.persistence.KraftNoteDatabase;
import com.example.kraftnote.persistence.daos.CategoryDao;
import com.example.kraftnote.persistence.entities.Category;
import com.example.kraftnote.persistence.repositories.contracts.IRepository;
import com.example.kraftnote.persistence.views.CategoryWithNotesCount;

import java.lang.ref.WeakReference;
import java.util.List;

public class CategoryRepository implements IRepository<Category> {
    private CategoryDao categoryDao;
    private LiveData<List<Category>> allCategories;
    private LiveData<List<CategoryWithNotesCount>> categoriesWithNotesCount;

    public CategoryRepository(Application application) {
        KraftNoteDatabase database = KraftNoteDatabase.getInstance(application);
        categoryDao = database.categoryDao();
        allCategories = categoryDao.getAll();
        categoriesWithNotesCount = categoryDao.getAllWithNotesCount();
    }

    @Override
    public void insert(Category category) {
        new InsertTask(categoryDao).execute(category);
    }

    @Override
    public void update(Category category) {
        new UpdateTask(categoryDao).execute(category);
    }

    public boolean nameExist(String categoryName) {
        return categoryDao.nameExists(categoryName);
    }

    @Override
    public void delete(Category category) {
        new DeleteTask(categoryDao).execute(category);
    }

    @Override
    public LiveData<List<Category>> getAll() {
        return allCategories;
    }

    public LiveData<List<CategoryWithNotesCount>> getCategoriesWithNotesCount() {
        return categoriesWithNotesCount;
    }

    private static class InsertTask extends CategoryMutationTask {
        public InsertTask(CategoryDao categoryDao) {
            super(categoryDao);
        }

        @Override
        protected Void doInBackground(Category... categories) {
            if (getCategoryDaoWeakReference().get() != null)
                getCategoryDaoWeakReference().get().insertAll(categories);

            return null;
        }
    }

    private static class UpdateTask extends CategoryMutationTask {
        public UpdateTask(CategoryDao categoryDao) {
            super(categoryDao);
        }

        @Override
        protected Void doInBackground(Category... categories) {
            if (getCategoryDaoWeakReference().get() != null)
                getCategoryDaoWeakReference().get().updateAll(categories);

            return null;
        }
    }

    private static class DeleteTask extends CategoryMutationTask {

        public DeleteTask(CategoryDao categoryDao) {
            super(categoryDao);
        }

        @Override
        protected Void doInBackground(Category... categories) {
            if (getCategoryDaoWeakReference().get() != null)
                getCategoryDaoWeakReference().get().deleteAll(categories);

            return null;
        }
    }

    private static abstract class CategoryMutationTask extends AsyncTask<Category, Void, Void> {
        private WeakReference<CategoryDao> categoryDaoWeakReference;

        public CategoryMutationTask(CategoryDao categoryDao) {
            this.categoryDaoWeakReference = new WeakReference<>(categoryDao);
        }

        WeakReference<CategoryDao> getCategoryDaoWeakReference() {
            return categoryDaoWeakReference;
        }
    }


}
