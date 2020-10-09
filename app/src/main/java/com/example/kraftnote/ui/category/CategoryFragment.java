package com.example.kraftnote.ui.category;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kraftnote.R;
import com.example.kraftnote.persistence.entities.Category;
import com.example.kraftnote.persistence.viewmodels.CategoryViewModel;
import com.example.kraftnote.persistence.views.CategoryWithNotesCount;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;
import java.util.Objects;

public class CategoryFragment extends Fragment {
    private CategoryViewModel categoryViewModel;
    private CategoryAdapter categoryAdapter;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView recyclerView = view.findViewById(R.id.category_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(false);

        categoryAdapter = new CategoryAdapter();
        recyclerView.setAdapter(categoryAdapter);
        categoryAdapter.setOnActionButtonClickedListener(this::onCategoryActionButtonClicked);

        categoryViewModel.getCategoriesWithNotesCount()
                .observe(getViewLifecycleOwner(), this::categoryWithNotesCountMutated);

        view.findViewById(R.id.button_fifth)
                .setOnClickListener(view1 -> NavHostFragment.findNavController(CategoryFragment.this)
                        .navigate(R.id.action_CategoryFragment_to_FirstFragment));
    }

    private void categoryWithNotesCountMutated(List<CategoryWithNotesCount> categoryWithNotesCounts) {
        categoryAdapter.setCategories(categoryWithNotesCounts);
    }

    private void onCategoryActionButtonClicked(CategoryWithNotesCount categoryWithNotesCount, CategoryAdapter.ButtonType buttonType) {
        switch (buttonType) {
            case EDIT:
                categoryViewModel.delete(categoryWithNotesCount.category);
                break;
            case DELETE:
                if (categoryWithNotesCount.notesCount > 0) {
                    new MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Cannot perform the action")
                            .setMessage("Cannot delete category having one or more notes.")
                            .setNeutralButton("Ok", null)
                            .show();
                    return;
                }

                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Confirmation Required")
                        .setMessage("Are you sure you want to delete this category? This process cannot be undone.")
                        .setPositiveButton("Confirm", (dialog, which) -> {
                            categoryViewModel.delete(categoryWithNotesCount.category);
                            Toast.makeText(getContext(), "Category Deleted" , Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                break;
            default:
                break;
        }
    }
}
