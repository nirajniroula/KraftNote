package com.example.kraftnote.ui.note;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.kraftnote.R;
import com.example.kraftnote.persistence.entities.Category;
import com.example.kraftnote.persistence.entities.LocationReminder;
import com.example.kraftnote.persistence.entities.Note;
import com.example.kraftnote.persistence.viewmodels.CategoryViewModel;
import com.example.kraftnote.ui.note.editor.NoteEditorImageFragment;
import com.example.kraftnote.ui.note.editor.NoteEditorReminderFragment;
import com.example.kraftnote.ui.note.editor.NoteEditorTitleBodyFragment;
import com.example.kraftnote.ui.note.editor.NoteEditorTodoFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AddEditNoteFragment extends Fragment {
    private static final String TAG = AddEditNoteFragment.class.getSimpleName();

    private View root;

    private NavController navController;
    private CategoryViewModel categoryViewModel;
    private MaterialButton closeEditorButton;
    private MaterialButton saveNoteButton;
    private List<Category> categories = new ArrayList<>();

    //meta
    private TextView locationReminderTextView;
    private TextView datetimeReminderTextView;

    // state
    private Note note;
    private LocationReminder locationReminder;
    private List<View> tabViews = new ArrayList<>();

    //tabs
    private TabLayout tabLayout;
    private int activeTab = 0;

    //components
    private NoteEditorTitleBodyFragment noteEditorTitleBodyFragment;
    private NoteEditorReminderFragment noteEditorReminderFragment;
    private NoteEditorImageFragment noteEditorImageFragment;
    private NoteEditorTodoFragment noteEditorTodoFragment;

    // This callback will only be called when AddUpdateNoteFragment is at least started
    private
    OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            AddEditNoteFragment.this.gotoNoteFragment();
        }
    };

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        requireActivity().getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), onBackPressedCallback);

        return inflater.inflate(R.layout.fragment_add_edit_note, container, false);
    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeProperties(view);
        listenEvents(view);
    }

    private void initializeProperties(@NonNull final View view) {
        note = new Note();

        noteEditorTitleBodyFragment = (NoteEditorTitleBodyFragment) getChildFragmentManager().findFragmentById(R.id.note_editor_title_body_fragment);
        noteEditorReminderFragment = (NoteEditorReminderFragment) getChildFragmentManager().findFragmentById(R.id.note_editor_reminder_fragment);
        noteEditorImageFragment = (NoteEditorImageFragment) getChildFragmentManager().findFragmentById(R.id.note_editor_images_fragment);
        noteEditorTodoFragment = (NoteEditorTodoFragment) getChildFragmentManager().findFragmentById(R.id.note_editor_todo_fragment);

        navController = NavHostFragment.findNavController(this);
        closeEditorButton = view.findViewById(R.id.close_editor_button);
        saveNoteButton = view.findViewById(R.id.save_note_button);

        tabLayout = view.findViewById(R.id.tabs);

        tabViews.add(view.findViewById(R.id.note_editor_title_body_fragment_wrapper));
        tabViews.add(view.findViewById(R.id.note_editor_reminder_fragment_wrapper));
        tabViews.add(view.findViewById(R.id.note_editor_images_fragment_wrapper));
        tabViews.add(view.findViewById(R.id.note_editor_todo_fragment_wrapper));
    }

    private void listenEvents(@NonNull final View view) {
        categoryViewModel.getAll().observe(getViewLifecycleOwner(), this::categoriesMutated);
        closeEditorButton.setOnClickListener(v -> gotoNoteFragment());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (activeTab == tab.getPosition()) return;

                for (View entry : tabViews) {
//                    Log.d(TAG, String.valueOf(entry));
                    entry.setVisibility(View.INVISIBLE);
                }

                activeTab = tab.getPosition();

                if (activeTab < tabViews.size())
                    tabViews.get(activeTab).setVisibility(View.VISIBLE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void categoriesMutated(List<Category> categories) {
        this.categories = categories;
    }

    private void gotoNoteFragment() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder
                .setTitle(R.string.confirmation_required)
                .setMessage(R.string.do_you_want_to_discard_the_changes)
                .setCancelable(false)
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, (dialog, which) -> navController.navigate(R.id.action_AddUpdateNoteFragment_to_NoteFragment))
                .show();
    }
}
