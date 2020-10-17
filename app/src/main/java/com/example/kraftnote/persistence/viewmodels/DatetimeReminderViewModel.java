package com.example.kraftnote.persistence.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.kraftnote.persistence.entities.DatetimeReminder;
import com.example.kraftnote.persistence.repositories.DatetimeReminderRepository;
import com.example.kraftnote.persistence.viewmodels.contracts.IViewModel;

import java.util.List;

public class DatetimeReminderViewModel extends AndroidViewModel implements IViewModel<DatetimeReminder> {
    private final DatetimeReminderRepository repository;
    private LiveData<List<DatetimeReminder>> reminders;

    public DatetimeReminderViewModel(@NonNull Application application) {
        super(application);
        repository = new DatetimeReminderRepository(application);
    }

    @Override
    public void insert(DatetimeReminder datetimeReminder) {
        repository.insert(datetimeReminder);
    }

    @Override
    public void update(DatetimeReminder datetimeReminder) {
        repository.update(datetimeReminder);
    }

    @Override
    public void delete(DatetimeReminder datetimeReminder) {
        repository.delete(datetimeReminder);
    }

    @Override
    public LiveData<List<DatetimeReminder>> getAll() {
        return reminders;
    }
}
