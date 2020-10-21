package com.example.kraftnote.ui.note.editor.components.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.kraftnote.R;
import com.example.kraftnote.databinding.DialogSetNoteCategoryBinding;
import com.example.kraftnote.persistence.entities.Category;

import java.util.ArrayList;
import java.util.List;

public class ChangeCategoryDialog extends DialogFragment {
    private AlertDialog dialog;
    private Category category;
    private OnCategorySelectedListener listener;
    private DialogSetNoteCategoryBinding binding;
    private CategoryAdapter adapter;
    private List<Category> categories = new ArrayList<>();
    private Runnable onActivityCreatedCallback;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        initializeProperties();
        listenEvents();
        return dialog;
    }

    private void initializeProperties() {
        binding = DialogSetNoteCategoryBinding.inflate(
                getLayoutInflater(), null, false);
        dialog = createDialog();
        adapter = new CategoryAdapter(requireContext(), R.layout.component_category_spinner_item);
        binding.categorySpinner.setAdapter(adapter);
    }

    private void listenEvents() {
        binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = adapter.getCategoryAt(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = null;
            }
        });
    }

    @NonNull
    public AlertDialog createDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        return builder.setView(binding.getRoot())
                .setTitle(R.string.change_category)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(
                        R.string.set,
                        ((dialog, which) -> {
                            if (listener == null) return;

                            listener.onCategorySelected(getCategory());
                        })
                ).create();
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        adapter.setCategoryList(categories);
    }

    public void setCategory(Category category) {
        this.category = category;
        adapter.selectCategory(category);
    }

    public Category getCategory() {
        return category;
    }

    public void setOnCategorySelectedListener(OnCategorySelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (onActivityCreatedCallback != null) {
            onActivityCreatedCallback.run();
        }
    }

    public void setOnActivityCreated(Runnable onActivityCreatedCallback) {
        this.onActivityCreatedCallback = onActivityCreatedCallback;
    }

    private final class CategoryAdapter extends ArrayAdapter<String> {
        private List<Category> categoryList = new ArrayList<>();

        public CategoryAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        @Override
        public int getCount() {
            return categoryList.size();
        }

        @Nullable
        @Override
        public String getItem(int position) {
            return categoryList.get(position).getName();
        }

        public void setCategoryList(List<Category> categoryList) {
            this.categoryList = categoryList;

            final String[] categories = categoryList.stream().map(Category::getName).toArray(String[]::new);

            clear();
            addAll(categories);
        }

        private void selectCategory(Category category) {
            int idx = -1;

            for (int i = 0; i < categoryList.size(); i++) {
                if(categoryList.get(i).getId().equals(category.getId())) {
                    idx = i;
                    break;
                }
            }

            if(idx < 0) return;

            binding.categorySpinner.setSelection(idx);
        }

        private Category getCategoryAt(int position) {
            return categoryList.get(position);
        }
    }

    public interface OnCategorySelectedListener {
        void onCategorySelected(Category category);
    }
}
