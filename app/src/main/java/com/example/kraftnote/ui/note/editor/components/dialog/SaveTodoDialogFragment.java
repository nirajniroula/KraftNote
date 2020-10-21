package com.example.kraftnote.ui.note.editor.components.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.ParcelableSpan;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.kraftnote.R;
import com.example.kraftnote.databinding.FragmentTodoCreateEditDialogBinding;
import com.example.kraftnote.persistence.entities.Todo;
import com.example.kraftnote.utils.watchers.PlainTextFormatWatcher;

public class SaveTodoDialogFragment extends DialogFragment {
    private FragmentTodoCreateEditDialogBinding binding;
    private OnSaveTodoListener listener;
    private AlertDialog dialog;
    private Button addButton;
    private Todo todo;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        binding = FragmentTodoCreateEditDialogBinding.inflate(getLayoutInflater(), null, false);

        dialog = builder.setView(binding.getRoot())
                .setTitle(
                        getTodo() == null
                                ? R.string.add_todo
                                : R.string.update_todo
                )
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(
                        getTodo() == null ? R.string.add : R.string.update,
                        ((dialog, which) -> {
                            if (listener == null) return;

                            if (getTodo() == null) {
                                listener.save(getTodoText());
                                return;
                            }

                            getTodo().setTask(getTodoText());
                            listener.save(null);
                        })
                ).create();

        setCancelable(false);

        listenEvents();

        return dialog;
    }

    private void listenEvents() {
        binding.todoText.addTextChangedListener(
                new PlainTextFormatWatcher(binding.todoText, this::toggleAddButton));

        dialog.setOnShowListener(alertDialog -> {
            addButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

            binding.todoText.setText(
                    getTodo() != null
                            ? getTodo().getTask()
                            : null
            );

            toggleAddButton();
        });
    }

    private void toggleAddButton() {
        if (addButton == null) return;

        addButton.setEnabled(true);
        binding.todoText.setError(null);

        addButton.setEnabled(isTodoTextValid());
    }

    private boolean isTodoTextValid() {
        return binding.todoText.getText() != null
                && binding.todoText.getText().toString().trim().length() > 0;
    }

    private String getTodoText() {
        if (binding.todoText.getText() != null)
            return binding.todoText.getText().toString().trim();

        return "";
    }

    public Todo getTodo() {
        return todo;
    }

    public void setTodo(Todo todo) {
        this.todo = todo;
    }

    public void setOnSaveTodoListener(OnSaveTodoListener listener) {
        this.listener = listener;
    }


    public interface OnSaveTodoListener {
        void save(String text);
    }
}
