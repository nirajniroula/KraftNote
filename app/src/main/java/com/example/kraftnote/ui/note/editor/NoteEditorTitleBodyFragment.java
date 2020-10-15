package com.example.kraftnote.ui.note.editor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kraftnote.R;
import com.example.kraftnote.databinding.FragmentNoteEditorTitleBodyBinding;

public class NoteEditorTitleBodyFragment extends Fragment {
    private static final String TAG = NoteEditorTitleBodyFragment.class.getSimpleName();

    private LinearLayout noteRemindersLinearLayout;

    private TextView locationReminderTextView;
    private TextView datetimeReminderTextView;

    private NoteTitleText noteTitleInputTextLayout;
    private NoteBodyText noteBodyTextInputText;

    private View root;
    private FragmentNoteEditorTitleBodyBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_editor_title_body, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentNoteEditorTitleBodyBinding.bind(view);
        root = binding.getRoot();

        initializeProperties();
        listenEvents();
    }

    private void initializeProperties() {
        noteRemindersLinearLayout = root.findViewById(R.id.note_reminders_linear_layout);
        locationReminderTextView = root.findViewById(R.id.location_reminder_text_view);
        datetimeReminderTextView = root.findViewById(R.id.datetime_reminder_text_view);
        noteTitleInputTextLayout = root.findViewById(R.id.note_title);
        noteBodyTextInputText = root.findViewById(R.id.editor_body_text);
    }

    private void listenEvents() {

    }
}
