package com.example.kraftnote.ui.category;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.kraftnote.R;
import com.example.kraftnote.persistence.entities.Category;
import com.example.kraftnote.persistence.viewmodels.CategoryViewModel;
import com.example.kraftnote.persistence.views.CategoryWithNotesCount;
import com.example.kraftnote.ui.category.components.CategoryRecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {
    private NavController navController;
    private CategoryViewModel categoryViewModel;
    private CategoryRecyclerView categoryRecyclerView;
    private FloatingActionButton addCategoryFab;
    private SaveCategoryDialogFragment saveCategoryDialogFragment;
    List<CategoryWithNotesCount> categoryWithNotesCounts = new ArrayList<>();

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

        initializeProperties();
        findViews(view);
        listenEvents();
    }

    private void initializeProperties() {
        navController = NavHostFragment.findNavController(this);
        saveCategoryDialogFragment = new SaveCategoryDialogFragment();
    }

    private void findViews(View view) {
        addCategoryFab = view.findViewById(R.id.add_category_fab);
        categoryRecyclerView = view.findViewById(R.id.category_recycler_view);
    }

    private void listenEvents() {
        categoryViewModel.getCategoriesWithNotesCount()
                .observe(getViewLifecycleOwner(), this::categoryWithNotesCountMutated);

        categoryRecyclerView.onEditButtonClicked(this::onEditRequest);
        categoryRecyclerView.onDeleteButtonClicked(this::onDeleteRequest);
        saveCategoryDialogFragment.onAddRequest(this::onSaveCategoryRequest);
        saveCategoryDialogFragment.setCheckUniqueCallback(this::checkCategoryNameIsUnique);
        addCategoryFab.setOnClickListener((View v) -> onCreateRequest());
    }

    private Boolean checkCategoryNameIsUnique(String categoryName, Category category) {
        boolean isUnique = true;

        for (CategoryWithNotesCount categoryWithNotesCount : categoryWithNotesCounts) {
            Category _category = categoryWithNotesCount.getCategory();

            if (category != null && category.getId() == _category.getId()) continue;

            isUnique = !categoryWithNotesCount
                    .getCategory().getName().trim().toLowerCase()
                    .equals(categoryName.trim().toLowerCase());

            if (!isUnique) break;
        }

        return isUnique;
    }

    private void onSaveCategoryRequest(Category category) {
        if (category.getCreatedAt() == null) {
            categoryViewModel.insert(category);
            Toast.makeText(getContext(), R.string.category_added, Toast.LENGTH_SHORT).show();
            return;
        }

        categoryViewModel.update(category);
        Toast.makeText(getContext(), R.string.category_updated, Toast.LENGTH_SHORT).show();
    }

    private void openAddCategoryDialog() {
        saveCategoryDialogFragment.show(getChildFragmentManager(), null);
    }

    private void onCreateRequest() {
        saveCategoryDialogFragment.setCategory(null);
        openAddCategoryDialog();
    }


    private void onEditRequest(CategoryWithNotesCount categoryWithNotesCount) {
        saveCategoryDialogFragment.setCategory(categoryWithNotesCount.getCategory());
        openAddCategoryDialog();
    }

    private void onDeleteRequest(CategoryWithNotesCount categoryWithNotesCount) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        if (categoryWithNotesCount.getNotesCount() > 0) {
            builder
                    .setTitle(R.string.unperformable_action)
                    .setMessage(R.string.cannot_delete_category_having_notes)
                    .setNeutralButton(R.string.ok, null)
                    .show();
            return;
        }

        builder.setTitle(R.string.confirmation_required)
                .setMessage(R.string.delete_category_question)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    categoryViewModel.delete(categoryWithNotesCount.getCategory());
                    Toast.makeText(getContext(), R.string.category_deleted, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void categoryWithNotesCountMutated(List<CategoryWithNotesCount> categoryWithNotesCounts) {
        this.categoryWithNotesCounts = categoryWithNotesCounts;
        categoryRecyclerView.setCategories(categoryWithNotesCounts);
    }

    private void gotoNoteFragment() {
        navController.navigate(R.id.action_CategoryFragment_to_NoteFragment);
    }
}
