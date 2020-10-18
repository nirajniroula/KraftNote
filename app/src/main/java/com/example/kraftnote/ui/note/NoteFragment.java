package com.example.kraftnote.ui.note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.kraftnote.R;
import com.example.kraftnote.databinding.FragmentNotesBinding;
import com.example.kraftnote.persistence.entities.Category;
import com.example.kraftnote.persistence.viewmodels.CategoryViewModel;
import com.example.kraftnote.persistence.viewmodels.NoteViewModel;
import com.example.kraftnote.persistence.views.NoteWithRelation;

import java.util.ArrayList;
import java.util.List;

public class NoteFragment extends Fragment {
    private FragmentNotesBinding binding;

    private NavController navController;
    private CategoryViewModel categoryViewModel;
    private NoteViewModel noteViewModel;
    private List<NoteWithRelation> notes = new ArrayList<>();

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        binding = FragmentNotesBinding.bind(view);

        return view;
    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeProperties();
        listenEvents();
    }

    private void initializeProperties() {
        navController = NavHostFragment.findNavController(this);
    }

    private void listenEvents() {
        categoryViewModel.getAll().observe(getViewLifecycleOwner(), this::categoriesMutated);
        noteViewModel.getAllWithRelation().observe(getViewLifecycleOwner(), this::notesMutated);
        binding.addNoteFab.setOnClickListener(this::addNoteRequest);
    }

    private void notesMutated(List<NoteWithRelation> notes) {
        this.notes = notes;
        binding.noteRecyclerView.setNotes(notes);
    }

    private void addNoteRequest(View view) {
        navController.navigate(R.id.action_NoteFragment_to_AddUpdateNoteFragment);
    }

    private void categoriesMutated(List<Category> categories) {
        binding.categoryTabs.sync(categories);
    }
}