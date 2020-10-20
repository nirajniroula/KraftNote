package com.example.kraftnote.persistence.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.kraftnote.persistence.entities.Note;
import com.example.kraftnote.persistence.repositories.NoteRepository;
import com.example.kraftnote.persistence.viewmodels.contracts.IViewModel;
import com.example.kraftnote.persistence.views.NoteWithRelation;

import java.util.List;

public class NoteViewModel extends AndroidViewModel implements IViewModel<Note> {
    private final NoteRepository repository;
    private LiveData<List<Note>> notes;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        notes = repository.getAll();
        allNotes = repository.getAll(true);
    }

    @Override
    public int insertSingle(Note note) {
        return repository.insertSingle(note);
    }

    @Override
    public void insert(Note note) {
        repository.insert(note);
    }

    @Override
    public void update(Note note) {
        repository.update(note);
    }

    @Override
    public void delete(Note note) {
        repository.delete(note);
    }

    public  Note findById(int id) {
        return repository.findById(id);
    }

    @Override
    public LiveData<List<Note>> getAll() {
        return notes;
    }

    public LiveData<List<NoteWithRelation>> getAllWithRelation() {
        return repository.getAllWithRelation();
    }

    public Note getLatestDraft() {
        return repository.getLatestDraft();
    }

    public LiveData<List<Note>> getAllWithDraft() {
        return allNotes;
    }
}
