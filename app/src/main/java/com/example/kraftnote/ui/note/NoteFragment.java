package com.example.kraftnote.ui.note;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.kraftnote.R;
import com.example.kraftnote.databinding.FragmentNotesBinding;
import com.example.kraftnote.persistence.entities.Category;
import com.example.kraftnote.persistence.entities.Note;
import com.example.kraftnote.persistence.entities.NoteFile;
import com.example.kraftnote.persistence.viewmodels.CategoryViewModel;
import com.example.kraftnote.persistence.viewmodels.NoteFileViewModel;
import com.example.kraftnote.persistence.viewmodels.NoteViewModel;
import com.example.kraftnote.persistence.views.NoteWithRelation;
import com.example.kraftnote.utils.FileHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NoteFragment extends Fragment {
    private static final String TAG = NoteFragment.class.getSimpleName();

    private FragmentNotesBinding binding;

    private NavController navController;
    private CategoryViewModel categoryViewModel;
    private NoteViewModel noteViewModel;
    private NoteFileViewModel noteFileViewModel;
    private List<NoteWithRelation> notes = new ArrayList<>();

    private FileHelper fileHelper;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteFileViewModel = new ViewModelProvider(this).get(NoteFileViewModel.class);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        binding = FragmentNotesBinding.bind(view);

        return view;
    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        hideKeyboardIfOpen();

        initializeProperties();
        listenEvents();
    }

    private void initializeProperties() {
        fileHelper = new FileHelper(requireContext());
        navController = NavHostFragment.findNavController(this);
    }

    private void listenEvents() {
        categoryViewModel.getAll().observe(getViewLifecycleOwner(), this::categoriesMutated);
        noteViewModel.getAllWithRelation().observe(getViewLifecycleOwner(), this::notesMutated);
        binding.addNoteFab.setOnClickListener(this::addNoteRequest);
        binding.noteRecyclerView.setOnDeleteNoteClickedListener(this::onDeleteNoteRequest);
        binding.noteRecyclerView.setOnEditNoteClickedListener(this::onEditNoteRequest);
    }

    private void onEditNoteRequest(NoteWithRelation noteWithRelation) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", noteWithRelation.getNote().getId());

        navController.navigate(R.id.action_NoteFragment_to_AddUpdateNoteFragment, bundle);
    }

    private void notesMutated(List<NoteWithRelation> notes) {
        this.notes = notes;
        binding.noteRecyclerView.setNotes(notes);
    }

    private void onDeleteNoteRequest(NoteWithRelation noteWithRelation) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder.setTitle(R.string.confirmation_required)
                .setMessage(R.string.delete_note_question)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    deleteFilesOfNote(noteWithRelation.getNote());
                    noteViewModel.delete(noteWithRelation.getNote());
                    Toast.makeText(getContext(), R.string.note_deleted, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void addNoteRequest(View view) {
        navController.navigate(R.id.action_NoteFragment_to_AddUpdateNoteFragment);
    }

    private void categoriesMutated(List<Category> categories) {
        binding.categoryTabs.sync(categories);
    }

    private void deleteFilesOfNote(Note note) {
        final List<NoteFile> noteFileList = noteFileViewModel.getAllFor(note.getId());

        if(noteFileList == null || noteFileList.size() == 0) return;

        final File[] files = noteFileList.stream().map(noteFile -> {
            return noteFile.isAudio()
                    ? fileHelper.getAudioDirectoryFile(noteFile.getLocation())
                    : fileHelper.getImageDirectoryFile(noteFile.getLocation());

        }).toArray(File[]::new);

        fileHelper.deleteFiles(files);
    }

    private void hideKeyboardIfOpen() {
        View v = requireActivity().getCurrentFocus();

        if (v == null) return;

        InputMethodManager imm = (InputMethodManager) requireActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}