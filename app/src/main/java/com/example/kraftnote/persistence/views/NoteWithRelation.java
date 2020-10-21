package com.example.kraftnote.persistence.views;

import androidx.room.DatabaseView;
import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.kraftnote.persistence.entities.Category;
import com.example.kraftnote.persistence.entities.DatetimeReminder;
import com.example.kraftnote.persistence.entities.LocationReminder;
import com.example.kraftnote.persistence.entities.Note;
import com.example.kraftnote.persistence.entities.NoteFile;
import com.example.kraftnote.persistence.entities.Todo;

import java.util.List;

@DatabaseView(
        "SELECT Categories.*, Notes.*, DatetimeReminders.*, LocationReminders.* " +
                "FROM NOTES " +
                "JOIN Categories ON Notes.note_category_id = Categories.category_id " +
                "LEFT JOIN DatetimeReminders ON DatetimeReminders.datetime_reminder_note_id = Notes.note_id " +
                "LEFT JOIN LocationReminders ON LocationReminders.location_reminder_note_id = Notes.note_id "
)
public class NoteWithRelation {
    @Embedded
    private Note note;

    @Embedded
    private Category category;

    @Relation(
            parentColumn = "note_id",
            entityColumn = "note_file_note_id"
    )
    private List<NoteFile> files;

    @Relation(
            parentColumn = "note_id",
            entityColumn = "todo_note_id"
    )
    private List<Todo> todos;

    @Embedded
    private DatetimeReminder datetimeReminder;

    @Embedded
    private LocationReminder locationReminder;

    public NoteWithRelation(Note note, Category category, List<NoteFile> files, List<Todo> todos, DatetimeReminder datetimeReminder, LocationReminder locationReminder) {
        this.note = note;
        this.category = category;
        this.files = files;
        this.todos = todos;
        this.datetimeReminder = datetimeReminder;
        this.locationReminder = locationReminder;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<NoteFile> getFiles() {
        return files;
    }

    public void setFiles(List<NoteFile> files) {
        this.files = files;
    }

    public List<Todo> getTodos() {
        return todos;
    }

    public void setTodos(List<Todo> todos) {
        this.todos = todos;
    }

    public DatetimeReminder getDatetimeReminder() {
        return datetimeReminder;
    }

    public void setDatetimeReminder(DatetimeReminder datetimeReminder) {
        this.datetimeReminder = datetimeReminder;
    }

    public LocationReminder getLocationReminder() {
        return locationReminder;
    }

    public void setLocationReminder(LocationReminder locationReminder) {
        this.locationReminder = locationReminder;
    }

    @Override
    public String toString() {
        return "NoteWithRelation{" +
                "note=" + note +
                ", category=" + category +
                ", files=" + files +
                ", todos=" + todos +
                ", datetimeReminder=" + datetimeReminder +
                ", locationReminder=" + locationReminder +
                '}';
    }
}
