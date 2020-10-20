package com.example.kraftnote.persistence.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.kraftnote.persistence.KraftNoteDatabase;
import com.example.kraftnote.persistence.daos.DatetimeReminderDao;
import com.example.kraftnote.persistence.entities.DatetimeReminder;
import com.example.kraftnote.persistence.repositories.contracts.IRepository;

import java.util.List;

public class DatetimeReminderRepository implements IRepository<DatetimeReminder> {
    private DatetimeReminderDao datetimeReminderDao;
    private LiveData<List<DatetimeReminder>> datetimeReminders;

    public DatetimeReminderRepository(Application application) {
        KraftNoteDatabase database = KraftNoteDatabase.getInstance(application);
        datetimeReminderDao = database.datetimeReminderDao();
        datetimeReminders = datetimeReminderDao.getAll();
    }

    @Override
    public int insertSingle(DatetimeReminder reminder) {
        return (int) datetimeReminderDao.insertSingle(reminder);
    }

    @Override
    public void insert(DatetimeReminder datetimeReminder) {
        datetimeReminderDao.insertAll(datetimeReminder);
    }

    @Override
    public void update(DatetimeReminder datetimeReminder) {
        datetimeReminderDao.updateAll(datetimeReminder);
    }

    @Override
    public void delete(DatetimeReminder datetimeReminder) {
        datetimeReminderDao.deleteAll(datetimeReminder);
    }

    @Override
    public LiveData<List<DatetimeReminder>> getAll() {
        return datetimeReminders;
    }
}
