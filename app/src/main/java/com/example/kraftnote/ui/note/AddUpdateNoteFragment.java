package com.example.kraftnote.ui.note;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.kraftnote.R;
import com.example.kraftnote.persistence.entities.Category;
import com.example.kraftnote.persistence.viewmodels.CategoryViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class AddUpdateNoteFragment extends Fragment {
    private static final String TAG = AddUpdateNoteFragment.class.getSimpleName();

    private NavController navController;
    private CategoryViewModel categoryViewModel;
    private MaterialButton closeEditorButton;
    private MaterialButton saveNoteButton;
    private List<Category> categories = new ArrayList<>();
    private FloatingActionButton openBottomSheetFab;
    private LinearLayout bottomSheet;
    private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;

    // This callback will only be called when AddUpdateNoteFragment is at least started
    private
    OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            AddUpdateNoteFragment.this.gotoNoteFragment();
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

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_update_note, container, false);
    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeProperties(view);
        listenEvents();
    }

    private void initializeProperties(@NonNull final View view) {
        navController = NavHostFragment.findNavController(this);
        closeEditorButton = view.findViewById(R.id.close_editor_button);
        saveNoteButton = view.findViewById(R.id.save_note_button);
        openBottomSheetFab = view.findViewById(R.id.open_bottom_sheet_fab);
        bottomSheet = view.findViewById(R.id.note_editor_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        bottomSheetBehavior.setPeekHeight(0);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void listenEvents() {
        categoryViewModel.getAll().observe(getViewLifecycleOwner(), this::categoriesMutated);

        closeEditorButton.setOnClickListener(v -> {
            gotoNoteFragment();
        });

        openBottomSheetFab.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            openBottomSheetFab.hide();
        });

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    openBottomSheetFab.show();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

//        bottomSheetBehavior;
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
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    navController.navigate(R.id.action_AddUpdateNoteFragment_to_NoteFragment);
                })
                .show();
    }
}