package com.example.kraftnote.ui.note.editor;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.kraftnote.R;
import com.example.kraftnote.databinding.FragmentNoteEditorTodoBinding;
import com.example.kraftnote.persistence.entities.Todo;
import com.example.kraftnote.persistence.viewmodels.TodoViewModel;
import com.example.kraftnote.ui.note.contracts.NoteEditorChildBaseFragment;
import com.example.kraftnote.ui.note.editor.components.SaveTodoDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NoteEditorTodoFragment extends NoteEditorChildBaseFragment {
    private static final String TAG = NoteEditorTodoFragment.class.getSimpleName();

    private FragmentNoteEditorTodoBinding binding;
    private SaveTodoDialogFragment saveTodoDialogFragment;
    private TodoViewModel todoViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);

        return inflater.inflate(R.layout.fragment_note_editor_todo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentNoteEditorTodoBinding.bind(view);

        initializeProperties();
        listenEvents();
    }

    private void initializeProperties() {
        saveTodoDialogFragment = new SaveTodoDialogFragment();
    }

    private void listenEvents() {
        todoViewModel.getAll().observe(getViewLifecycleOwner(), this::todosMutated);

        binding.todoRecyclerView.setOnTodoCheckChangedListener((todo, isChecked) -> {
            todo.setCompleted(isChecked);
            todoViewModel.update(todo);
            binding.todoRecyclerView.updateTodo(todo);
        });

        saveTodoDialogFragment.setOnSaveTodoListener(todo -> {
            if (saveTodoDialogFragment.getTodo() == null) {
                createTodo(todo);
                return;
            }

            updateTodo(saveTodoDialogFragment.getTodo());
        });

        binding.todoRecyclerView.setOnToolBarMenuEditClicked(todo -> {
            saveTodoDialogFragment.setTodo(todo);
            saveTodoDialogFragment.show(getChildFragmentManager(), null);
        });

        binding.todoRecyclerView.setOnToolBarMenuDeleteClicked(this::todoDeletionRequested);

        binding.addTodoButton.setOnClickListener(v -> {
            saveTodoDialogFragment.setTodo(null);
            saveTodoDialogFragment.show(getChildFragmentManager(), null);
        });
    }

    private void updateTodo(Todo todo) {
        todoViewModel.update(todo);
        binding.todoRecyclerView.updateTodo(todo);
        Toast.makeText(getContext(), R.string.todo_updated, Toast.LENGTH_SHORT).show();
    }

    private void createTodo(String text) {
        Todo todo = new Todo(text, getNote().getId());
        int id = todoViewModel.insertSingle(todo);
        todo.setId(id);
        binding.todoRecyclerView.addTodo(todo);
        Toast.makeText(getContext(), R.string.todo_added, Toast.LENGTH_SHORT).show();
    }

    private void todosMutated(List<Todo> todos) {
        ArrayList<Todo> collect = todos.stream()
                .filter(todo -> Objects.equals(todo.getNoteId(), getNote().getId()))
                .collect(Collectors.toCollection(ArrayList<Todo>::new));

        binding.todoRecyclerView.setTodos(collect);
    }

    private void todoDeletionRequested(Todo todo) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder.setTitle(R.string.confirmation_required)
                .setMessage(R.string.delete_todo_question)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    todoViewModel.delete(todo);
                    binding.todoRecyclerView.removeTodo(todo);
                    Toast.makeText(getContext(), R.string.todo_deleted, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
