package com.example.kraftnote.persistence.entities.contracts;

public interface IBody {
    Integer getId();
    void setId(Integer id);
    BodyType getType();
    void setType(BodyType type);
    String getContent();
    void setContent(String content);
    int getPriority();
    void setPriority(int priority);
    int getNoteId();
    void setNoteId(int noteId);
}
