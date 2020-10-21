package com.example.kraftnote.persistence.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.kraftnote.persistence.KraftNoteDatabase;
import com.example.kraftnote.persistence.daos.NoteDao;
import com.example.kraftnote.persistence.entities.Note;
import com.example.kraftnote.persistence.repositories.contracts.IRepository;
import com.example.kraftnote.persistence.views.NoteWithRelation;

import java.lang.ref.WeakReference;
import java.util.List;

public class NoteRepository implements IRepository<Note> {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        KraftNoteDatabase database = KraftNoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAll();
    }

    @Override
    public int insertSingle(Note note) {
        return (int) noteDao.insertSingle(note);
    }

    @Override
    public void insert(Note note) {
        noteDao.insertAll(note);
        //new InsertTask(noteDao).execute(note);
    }

    @Override
    public void update(Note note) {
        new UpdateTask(noteDao).execute(note);
    }

    @Override
    public void delete(Note note) {
        new DeleteTask(noteDao).execute(note);
    }

    @Override
    public LiveData<List<Note>> getAll() {
        return allNotes;
    }

    public LiveData<List<NoteWithRelation>> getAllWithRelation() {
        return noteDao.getAllWithRelation();
    }

    public Note findById(int id) {
        return noteDao.findById(id);
    }

    public Note getLatestDraft() {
        return noteDao.getLatestDraft();
    }

    public LiveData<List<Note>> getAll(boolean includeDraft) {
        return noteDao.getAll(includeDraft ? 1 : 0);
    }

    private static class InsertTask extends NoteMutationTask {
        public InsertTask(NoteDao noteDao) {
            super(noteDao);
        }

        @Override
        protected Void doInBackground(Note... notes) {
            if (getNoteDaoWeakReference().get() != null)
                getNoteDaoWeakReference().get().insertAll(notes);

            return null;
        }
    }

    private static class UpdateTask extends NoteMutationTask {
        public UpdateTask(NoteDao noteDao) {
            super(noteDao);
        }

        @Override
        protected Void doInBackground(Note... notes) {
            if (getNoteDaoWeakReference().get() != null)
                getNoteDaoWeakReference().get().updateAll(notes);

            return null;
        }
    }

    private static class DeleteTask extends NoteMutationTask {

        public DeleteTask(NoteDao noteDao) {
            super(noteDao);
        }

        @Override
        protected Void doInBackground(Note... notes) {
            if (getNoteDaoWeakReference().get() != null)
                getNoteDaoWeakReference().get().deleteAll(notes);

            return null;
        }
    }

    private static abstract class NoteMutationTask extends AsyncTask<Note, Void, Void> {
        private WeakReference<NoteDao> noteDaoWeakReference;

        public NoteMutationTask(NoteDao noteDao) {
            this.noteDaoWeakReference = new WeakReference<>(noteDao);
        }

        WeakReference<NoteDao> getNoteDaoWeakReference() {
            return noteDaoWeakReference;
        }
    }
}
