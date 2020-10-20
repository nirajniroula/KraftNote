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
    private final NoteRepository noteRepository;
    private LiveData<List<Note>> notes;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        notes = noteRepository.getAll();
        allNotes = noteRepository.getAll(true);
    }

    @Override
    public void insert(Note note) {
        noteRepository.insert(note);
    }

    @Override
    public void update(Note note) {
        noteRepository.update(note);
    }

    @Override
    public void delete(Note note) {
        noteRepository.delete(note);
    }

    public  Note findById(int id) {
        return noteRepository.findById(id);
    }

    @Override
    public LiveData<List<Note>> getAll() {
        return notes;
    }

    public LiveData<List<NoteWithRelation>> getAllWithRelation() {
        return noteRepository.getAllWithRelation();
    }

    public Note getLatestDraft() {
        return noteRepository.getLatestDraft();
    }

    public LiveData<List<Note>> getAllWithDraft() {
        return allNotes;
    }
}
