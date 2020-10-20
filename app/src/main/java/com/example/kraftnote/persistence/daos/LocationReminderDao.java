package com.example.kraftnote.persistence.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.kraftnote.persistence.entities.LocationReminder;

import java.util.List;

@Dao
public interface LocationReminderDao {
    @Insert
    void insertAll(LocationReminder... noteFiles);

    @Update
    void updateAll(LocationReminder... noteFiles);

    @Delete
    void deleteAll(LocationReminder... noteFiles);

    @Query("SELECT * FROM LocationReminders ORDER BY location_reminder_created_at DESC")
    LiveData<List<LocationReminder>> getAll();

    @Insert
    long insertSingle(LocationReminder locationReminder);
}
