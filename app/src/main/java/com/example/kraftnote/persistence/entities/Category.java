package com.example.kraftnote.persistence.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;

@Entity(tableName = "Categories", indices = { @Index(value = { "name" }, unique = true)})
public class Category extends BaseEntity {

    private String name;

    public Category(String name) {
        this.name = name;
//        this(name, new Date());
    }

//    @Ignore
//    public Category(String name, Date createdAt) {
//        this.name = name;
//        setCreatedAt(createdAt);
//    }

    public String getName() {
        return name;
    }

    @Ignore
    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", createdAt=" + createdAt +
                '}';
    }
}
