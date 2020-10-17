package com.example.kraftnote.persistence.viewmodels.notes.editor;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.kraftnote.persistence.entities.Category;
import com.example.kraftnote.persistence.entities.DatetimeReminder;
import com.example.kraftnote.persistence.entities.LocationReminder;
import com.example.kraftnote.persistence.entities.Note;
import com.example.kraftnote.persistence.entities.NoteFile;
import com.example.kraftnote.persistence.entities.Todo;

import java.util.ArrayList;
import java.util.List;

public class NoteEditorViewModel extends AndroidViewModel {
    private final MutableLiveData<Note> note;
    private final MutableLiveData<Category> category;
    private final MutableLiveData<List<Todo>> todos;
    private final MutableLiveData<List<NoteFile>> files;
    private final MutableLiveData<DatetimeReminder> datetimeReminder;
    private final MutableLiveData<LocationReminder> locationReminder;

    public NoteEditorViewModel(@NonNull Application application) {
        super(application);
        note = new MutableLiveData<>(null);
        category = new MutableLiveData<>(null);
        todos = new MutableLiveData<>(new ArrayList<>());
        files = new MutableLiveData<>(new ArrayList<>());
        datetimeReminder = new MutableLiveData<>(null);
        locationReminder = new MutableLiveData<>(null);
    }

    public LiveData<Note> getNote() {
        return note;
    }

    public LiveData<Category> getCategory() {
        return category;
    }

    public LiveData<List<Todo>> getTodos() {
        return todos;
    }

    public LiveData<List<NoteFile>> getFiles() {
        return files;
    }

    public LiveData<DatetimeReminder> getDatetimeReminder() {
        return datetimeReminder;
    }

    public LiveData<LocationReminder> getLocationReminder() {
        return locationReminder;
    }

    public void setNote(Note note) {
        this.note.setValue(note);
    }

    public void setCategory(Category category) {
        this.category.setValue(category);
    }

    public void setTodos(List<Todo> todos) {
        this.todos.setValue(todos);
    }

    public void addTodo(Todo todo) {
        if(todos.getValue() == null) {
            todos.setValue(new ArrayList<>());
        }

        todos.getValue().add(todo);
        todos.setValue(todos.getValue());
    }

    public void removeTodo(Todo todo){
        if(todos.getValue() == null) return;

        todos.getValue().remove(todo);
        todos.setValue(todos.getValue());
    }

    public void setFiles(List<NoteFile> files) {
        this.files.setValue(files);
    }

    public void addFile(NoteFile file) {
        if(files.getValue() == null) {
            files.setValue(new ArrayList<>());
        }
        files.getValue().add(file);
        files.setValue(files.getValue());
    }

    public void removeFile(NoteFile file){
        if(files.getValue() == null) return;

        files.getValue().remove(file);
        files.setValue(files.getValue());
    }

    public void setDatetimeReminder(DatetimeReminder datetimeReminder) {
        this.datetimeReminder.setValue(datetimeReminder);
    }

    public void setLocationReminder(LocationReminder locationReminder) {
        this.locationReminder.setValue(locationReminder);
    }

    public void reset() {
        note.setValue(null);
        category.setValue(null);
        todos.setValue(new ArrayList<>());
        files.setValue(new ArrayList<>());
        datetimeReminder.setValue(null);
        locationReminder.setValue(null);
    }
}
