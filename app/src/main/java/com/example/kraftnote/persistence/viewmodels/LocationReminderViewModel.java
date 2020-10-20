package com.example.kraftnote.persistence.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.kraftnote.persistence.entities.LocationReminder;
import com.example.kraftnote.persistence.repositories.LocationReminderRepository;
import com.example.kraftnote.persistence.viewmodels.contracts.IViewModel;

import java.util.List;

public class LocationReminderViewModel extends AndroidViewModel implements IViewModel<LocationReminder> {
    private LocationReminderRepository repository;
    private LiveData<List<LocationReminder>> locationReminders;

    public LocationReminderViewModel(@NonNull Application application) {
        super(application);
        repository = new LocationReminderRepository(application);
    }

    @Override
    public int insertSingle(LocationReminder locationReminder) {
        return repository.insertSingle(locationReminder);
    }

    @Override
    public void insert(LocationReminder locationReminder) {
        repository.insert(locationReminder);
    }

    @Override
    public void update(LocationReminder locationReminder) {
        repository.update(locationReminder);
    }

    @Override
    public void delete(LocationReminder locationReminder) {
        repository.delete(locationReminder);
    }

    @Override
    public LiveData<List<LocationReminder>> getAll() {
        return locationReminders;
    }
}
