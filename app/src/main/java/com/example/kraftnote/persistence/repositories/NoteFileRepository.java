package com.example.kraftnote.persistence.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.kraftnote.persistence.KraftNoteDatabase;
import com.example.kraftnote.persistence.daos.NoteFileDao;
import com.example.kraftnote.persistence.entities.NoteFile;
import com.example.kraftnote.persistence.repositories.contracts.IRepository;

import java.util.List;

public class NoteFileRepository implements IRepository<NoteFile> {
    private NoteFileDao noteFileDao;
    private LiveData<List<NoteFile>> noteFiles;

    public NoteFileRepository(Application application) {
        KraftNoteDatabase database = KraftNoteDatabase.getInstance(application);
        noteFileDao = database.noteFileDao();
        noteFiles = noteFileDao.getAll();
    }

    public void insert(NoteFile noteFile) {
        noteFileDao.insertAll(noteFile);
    }

    public void update(NoteFile noteFile) {
        noteFileDao.updateAll(noteFile);
    }

    public void delete(NoteFile noteFile) {
        noteFileDao.deleteAll(noteFile);
    }

    public LiveData<List<NoteFile>> getAllFor(int id) {
        return noteFileDao.getAllForNote(id);
    }

    public LiveData<List<NoteFile>> getAll() {
        return noteFiles;
    }
}
