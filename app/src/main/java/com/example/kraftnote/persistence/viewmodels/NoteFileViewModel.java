package com.example.kraftnote.persistence.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.kraftnote.persistence.entities.NoteFile;
import com.example.kraftnote.persistence.repositories.NoteFileRepository;
import com.example.kraftnote.persistence.viewmodels.contracts.IViewModel;

import java.util.List;

public class NoteFileViewModel extends AndroidViewModel implements IViewModel<NoteFile> {
    private final NoteFileRepository repository;
    private LiveData<List<NoteFile>> noteFiles;

    public NoteFileViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteFileRepository(application);
        noteFiles = repository.getAll();
    }

    @Override
    public void insert(NoteFile noteFile) {
        repository.insert(noteFile);
    }

    @Override
    public void update(NoteFile noteFile) {
        repository.update(noteFile);
    }

    @Override
    public void delete(NoteFile noteFile) {
        repository.delete(noteFile);
    }

    @Override
    public LiveData<List<NoteFile>> getAll() {
        return noteFiles;
    }

    public LiveData<List<NoteFile>> getAllFor(int id) {
        return repository.getAllFor(id);
    }
}
