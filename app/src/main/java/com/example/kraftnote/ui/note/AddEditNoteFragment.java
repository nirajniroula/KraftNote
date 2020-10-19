package com.example.kraftnote.ui.note;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.kraftnote.R;
import com.example.kraftnote.databinding.FragmentAddEditNoteBinding;
import com.example.kraftnote.persistence.entities.Category;
import com.example.kraftnote.persistence.viewmodels.CategoryViewModel;
import com.example.kraftnote.ui.note.editor.NoteEditorImageFragment;
import com.example.kraftnote.ui.note.editor.NoteEditorRecordingFragment;
import com.example.kraftnote.ui.note.editor.NoteEditorReminderFragment;
import com.example.kraftnote.ui.note.editor.NoteEditorTitleBodyFragment;
import com.example.kraftnote.ui.note.editor.NoteEditorTodoFragment;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddEditNoteFragment extends Fragment {
    private static final String TAG = AddEditNoteFragment.class.getSimpleName();

    private FragmentAddEditNoteBinding binding;

    private NavController navController;
    private CategoryViewModel categoryViewModel;
    private FragmentCollectionAdapter fragmentCollectionAdapter;

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

        View root = inflater.inflate(R.layout.fragment_add_edit_note, container, false);
        binding = FragmentAddEditNoteBinding.bind(root);

        return root;
    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeProperties();
        listenEvents();
    }

    private void initializeProperties() {
        fragmentCollectionAdapter = new FragmentCollectionAdapter(this);
        navController = NavHostFragment.findNavController(this);

        binding.viewpager.setAdapter(fragmentCollectionAdapter);

        new TabLayoutMediator(
                binding.tabs, binding.viewpager,
                (tab, position) -> tab.setText(fragmentCollectionAdapter.getFragmentTitle(position))
        ).attach();
    }

    private void listenEvents() {
        categoryViewModel.getAll().observe(getViewLifecycleOwner(), this::categoriesMutated);
        binding.closeEditorButton.setOnClickListener(v -> gotoNoteFragment());
    }

    private void categoriesMutated(List<Category> categories) {
        Log.d(TAG, "Category Mutated " + categories.size());
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

    private static final class FragmentCollectionAdapter extends FragmentStateAdapter {
        private Map<Integer, Fragment> fragmentMap = new HashMap<>();
        private final int[] titles = new int[]{
                R.string.note,
                R.string.reminders,
                R.string.images,
                R.string.todos,
                R.string.recordings
        };

        public FragmentCollectionAdapter(@NonNull Fragment fragment) {
            super(fragment);
            fragmentMap.put(0, new NoteEditorTitleBodyFragment());
            fragmentMap.put(1, new NoteEditorReminderFragment());
            fragmentMap.put(2, new NoteEditorImageFragment());
            fragmentMap.put(3, new NoteEditorTodoFragment());
            fragmentMap.put(4, new NoteEditorRecordingFragment());
        }

        public int getFragmentTitle(int position) {
            return titles[position];
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = fragmentMap.get(position);

            if (fragment != null) return fragment;

            return new Fragment();
        }

        @Override
        public int getItemCount() {
            return fragmentMap.size();
        }
    }
}
