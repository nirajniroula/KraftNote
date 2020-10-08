package com.example.kraftnote.persistence.entities;

import androidx.room.Entity;
import androidx.room.Index;

@Entity(tableName = "Tags", indices = {@Index(value = {"name"}, unique = true)})
public class Tag extends BaseEntity {
    private String name;

    public Tag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
