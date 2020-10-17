package com.example.kraftnote.persistence.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.kraftnote.persistence.converters.CreatedAtConverter;

import java.util.Date;

@Entity(tableName = "Tags", indices = {@Index(value = {"tag_name"}, unique = true)})
public class Tag {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tag_id")
    protected Integer id;

    @ColumnInfo(name = "tag_name")
    private String name;

    @TypeConverters(CreatedAtConverter.class)
    @ColumnInfo(name = "tag_created_at", defaultValue = "CURRENT_TIMESTAMP")
    protected Date createdAt;

    @Ignore
    public Tag() {
    }

    public Tag(Integer id, String name, Date createdAt) {
        this.id = id;
        this.name = name;
        setCreatedAt(createdAt);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = (createdAt == null) ? new Date() : createdAt;
    }
}
