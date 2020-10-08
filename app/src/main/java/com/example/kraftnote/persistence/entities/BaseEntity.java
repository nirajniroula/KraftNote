package com.example.kraftnote.persistence.entities;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.kraftnote.persistence.converters.DateConverter;

import java.util.Date;

public abstract class BaseEntity {
    @PrimaryKey(autoGenerate = true)
    protected int id;

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    protected Date createdAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
