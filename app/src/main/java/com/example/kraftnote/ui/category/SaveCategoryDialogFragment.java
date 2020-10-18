package com.example.kraftnote.ui.category;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.kraftnote.R;
import com.example.kraftnote.persistence.entities.Category;
import com.google.android.material.textfield.TextInputEditText;

public class SaveCategoryDialogFragment extends DialogFragment {
    private TextInputEditText nameEditText;
    private Button addButton;
    private AlertDialog dialog;
    private Category category;
    private SaveCategoryCallback saveCategoryCallback;
    private CategoryNameUniqueCheckCallback categoryNameUniqueCheckCallback;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.category_add_edit_dialog, null);
        nameEditText = view.findViewById(R.id.category_name);
        setCancelable(false);

        builder.setView(view)
                .setTitle(
                        getCategory() == null
                                ? R.string.add_category
                                : R.string.update_category
                )
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(
                        getCategory() == null ? R.string.add : R.string.update,
                        ((dialog, which) -> {
                            if (saveCategoryCallback == null) return;

                            if (getCategory() == null) {
                                saveCategoryCallback.save(new Category(getCategoryName()));
                                return;
                            }

                            getCategory().setName(getCategoryName());

                            saveCategoryCallback.save(getCategory());
                        }));

        dialog = builder.create();

        listenEvents();

        return dialog;
    }

    private void listenEvents() {
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                toggleAddButton();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                toggleAddButton();
            }
        });

        dialog.setOnShowListener(alertDialog -> {
            addButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

            nameEditText.setText(
                    getCategory() != null
                            ? getCategory().getName()
                            : null
            );

            toggleAddButton();
        });
    }

    public void setOnCategoryAddRequest(SaveCategoryCallback saveCategoryCallback) {
        this.saveCategoryCallback = saveCategoryCallback;
    }

    public String getCategoryName() {
        if (nameEditText.getText() != null)
            return nameEditText.getText().toString().trim();

        return "";
    }

    public boolean isCategoryNameValid() {
        return getCategoryName().length() > 0;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    private void toggleAddButton() {
        if (addButton == null) return;

        addButton.setEnabled(true);
        nameEditText.setError(null);

        if (!isCategoryNameValid()) {
            addButton.setEnabled(false);
            return;
        }

        if (categoryNameUniqueCheckCallback == null) {
            return;
        }

        final Boolean isUnique = categoryNameUniqueCheckCallback.run(getCategoryName(), getCategory());

        nameEditText.setError(
                isUnique
                        ? null
                        : getResources().getString(R.string.error_helper_text_add_category)
        );

        addButton.setEnabled(isUnique);
    }

    public void setCheckUniqueCallback(CategoryNameUniqueCheckCallback categoryNameUniqueCheckCallback) {
        this.categoryNameUniqueCheckCallback = categoryNameUniqueCheckCallback;
    }

    public interface CategoryNameUniqueCheckCallback {
        boolean run(String categoryName, Category category);
    }

    public interface SaveCategoryCallback {
        void save(Category category);
    }
}
