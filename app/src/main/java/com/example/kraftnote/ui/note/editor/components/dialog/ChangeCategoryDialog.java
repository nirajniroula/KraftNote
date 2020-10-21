package com.example.kraftnote.ui.note.editor.components.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.kraftnote.R;
import com.example.kraftnote.persistence.entities.Category;
import com.example.kraftnote.persistence.viewmodels.CategoryViewModel;

import java.util.List;

public class ChangeCategoryDialog extends DialogFragment {
    private AlertDialog dialog;
    private Category category;
    private List<Category> categoryList;
    private CategoryViewModel categoryViewModel;
    private OnCategorySelectedListener listener;
    private View view;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        initializeProperties();

        return dialog;
    }

    private void initializeProperties() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        dialog = builder.setView(view)
                .setTitle(R.string.change_category)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(
                        R.string.set,
                        ((dialog, which) -> {
                            if (listener == null) return;

                            listener.onCategorySelected(getCategory());
                        })
                ).create();

        setCancelable(true);
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public interface OnCategorySelectedListener {
        void onCategorySelected(Category category);
    }
}
