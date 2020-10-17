package com.example.kraftnote.persistence.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.kraftnote.persistence.converters.CreatedAtConverter;

import java.util.Date;

@Entity(tableName = "Categories", indices = {@Index(value = {"category_name"}, unique = true)})
public class Category {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    protected Integer id;

    @ColumnInfo(name = "category_name")
    private String name;

    @TypeConverters(CreatedAtConverter.class)
    @ColumnInfo(name = "category_created_at", defaultValue = "CURRENT_TIMESTAMP")
    protected Date createdAt;

    @Ignore
    public Category() {
    }

    @Ignore
    public Category(String name) {
        this.name = name;
    }

    public Category(Integer id, String name, Date createdAt) {
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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = (createdAt == null) ? new Date() : createdAt;
    }
}
