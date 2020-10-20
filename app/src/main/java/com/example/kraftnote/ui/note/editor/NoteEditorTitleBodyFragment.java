package com.example.kraftnote.ui.note.editor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.kraftnote.R;
import com.example.kraftnote.databinding.FragmentNoteEditorTitleBodyBinding;
import com.example.kraftnote.persistence.entities.Note;
import com.example.kraftnote.ui.note.contracts.ViewPagerFragment;

public class NoteEditorTitleBodyFragment extends ViewPagerFragment {
    private static final String TAG = NoteEditorTitleBodyFragment.class.getSimpleName();

    private FragmentNoteEditorTitleBodyBinding binding;
    private MutableLiveData<Note> note;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_editor_title_body, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentNoteEditorTitleBodyBinding.bind(view);

        initializeProperties();
        listenEvents();
    }

    private void initializeProperties() {
        note = new MutableLiveData<>(new Note());
    }

    private void listenEvents() {
        binding.editorBodyText.getBody().observe(getViewLifecycleOwner(), body -> {

            if (note.getValue() == null) note.setValue(new Note());

            note.getValue().setBody(body);
            note.setValue(note.getValue());
        });

        binding.noteTitle.getTitle().observe(getViewLifecycleOwner(), name -> {

            if (note.getValue() == null) note.setValue(new Note());

            note.getValue().setName(name);
            note.setValue(note.getValue());
        });
    }

    public Note getNote() {
        return note.getValue();
    }
}
