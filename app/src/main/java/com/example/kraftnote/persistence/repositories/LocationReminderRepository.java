package com.example.kraftnote.persistence.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.kraftnote.persistence.KraftNoteDatabase;
import com.example.kraftnote.persistence.daos.LocationReminderDao;
import com.example.kraftnote.persistence.entities.LocationReminder;
import com.example.kraftnote.persistence.repositories.contracts.IRepository;

import java.util.List;

public class LocationReminderRepository implements IRepository<LocationReminder> {

    private LocationReminderDao locationReminderDao;
    private LiveData<List<LocationReminder>> locationReminders;

    public LocationReminderRepository(Application application) {
        KraftNoteDatabase database = KraftNoteDatabase.getInstance(application);
        locationReminderDao = database.locationReminderDao();
        locationReminders = locationReminderDao.getAll();
    }

    @Override
    public int insertSingle(LocationReminder locationReminder) {
        return (int) locationReminderDao.insertSingle(locationReminder);
    }

    @Override
    public void insert(LocationReminder locationReminder) {
        locationReminderDao.insertAll(locationReminder);
    }

    @Override
    public void update(LocationReminder locationReminder) {
        locationReminderDao.updateAll(locationReminder);
    }

    @Override
    public void delete(LocationReminder locationReminder) {
        locationReminderDao.deleteAll(locationReminder);
    }

    @Override
    public LiveData<List<LocationReminder>> getAll() {
        return locationReminders;
    }
}
