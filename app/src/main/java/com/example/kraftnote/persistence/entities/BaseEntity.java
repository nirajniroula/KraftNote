package com.example.kraftnote.persistence.entities;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.kraftnote.persistence.converters.CreatedAtConverter;

import java.util.Date;

public abstract class BaseEntity {
    @PrimaryKey(autoGenerate = true)
    protected Integer id;

    @TypeConverters(CreatedAtConverter.class)
    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    protected Date createdAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = (createdAt == null) ? new Date() : createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
