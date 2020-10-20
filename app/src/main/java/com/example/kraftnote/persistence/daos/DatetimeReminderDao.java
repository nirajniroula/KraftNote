package com.example.kraftnote.persistence.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.kraftnote.persistence.entities.DatetimeReminder;

import java.util.List;

@Dao
public interface DatetimeReminderDao {
    @Insert
    void insertAll(DatetimeReminder... noteFiles);

    @Update
    void updateAll(DatetimeReminder... noteFiles);

    @Delete
    void deleteAll(DatetimeReminder... noteFiles);

    @Query("SELECT * FROM DatetimeReminders ORDER BY datetime_reminder_created_at DESC")
    LiveData<List<DatetimeReminder>> getAll();

    @Insert
    long insertSingle(DatetimeReminder reminder);
}
