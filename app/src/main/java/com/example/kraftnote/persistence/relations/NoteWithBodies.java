package com.example.kraftnote.persistence.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.kraftnote.persistence.entities.Body;
import com.example.kraftnote.persistence.entities.Note;

import java.util.List;

public class NoteWithBodies {
    @Embedded
    private Note note;

    @Relation(
            parentColumn = "id",
            entityColumn = "note_id",
            entity = Body.class
    )
    private List<Body> bodies;

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public List<Body> getBodies() {
        return bodies;
    }

    public void setBodies(List<Body> bodies) {
        this.bodies = bodies;
    }
}
