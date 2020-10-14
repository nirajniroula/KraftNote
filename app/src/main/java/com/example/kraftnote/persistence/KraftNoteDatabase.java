package com.example.kraftnote.persistence;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.kraftnote.persistence.daos.CategoryDao;
import com.example.kraftnote.persistence.daos.NoteDao;
import com.example.kraftnote.persistence.daos.TagDao;
import com.example.kraftnote.persistence.entities.Category;
import com.example.kraftnote.persistence.entities.Note;
import com.example.kraftnote.persistence.entities.Tag;
import com.example.kraftnote.persistence.views.CategoryWithNotesCount;

import java.lang.ref.WeakReference;

@Database(entities = {Category.class, Note.class, Tag.class},
        views = {CategoryWithNotesCount.class},
        version = 1
)
public abstract class KraftNoteDatabase extends RoomDatabase {
    private static volatile KraftNoteDatabase INSTANCE;

    private static RoomDatabase.Callback PersistenceCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new DatabaseSeeder(INSTANCE).execute();
        }
    };

    public abstract CategoryDao categoryDao();

    public abstract NoteDao noteDao();

    public abstract TagDao tagDao();

    public static KraftNoteDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (KraftNoteDatabase.class) {
                INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                        KraftNoteDatabase.class)
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .addCallback(PersistenceCallback)
                        .build();
            }
        }

        return INSTANCE;
    }


    private static class DatabaseSeeder extends AsyncTask<Void, Void, Void> {

        private WeakReference<KraftNoteDatabase> databaseWeakReference;

        public DatabaseSeeder(KraftNoteDatabase database) {
            this.databaseWeakReference = new WeakReference<>(database);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (databaseWeakReference.get() == null) return null;

            databaseWeakReference.get().categoryDao().insertAll(
                    new Category("Personal"),
                    new Category("Work"),
                    new Category("Study"),
                    new Category("Event")
            );

            return null;
        }
    }
}
