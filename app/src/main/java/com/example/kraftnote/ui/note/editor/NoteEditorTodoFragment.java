package com.example.kraftnote.ui.note.editor;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.kraftnote.R;
import com.example.kraftnote.databinding.FragmentNoteEditorTodoBinding;
import com.example.kraftnote.persistence.entities.Todo;

import java.util.ArrayList;
import java.util.List;

public class NoteEditorTodoFragment extends Fragment {
    private static final String TAG = NoteEditorTodoFragment.class.getSimpleName();

    private FragmentNoteEditorTodoBinding binding;
    private MutableLiveData<List<Todo>> todos;
    private SaveTodoDialogFragment saveTodoDialogFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        todos = new MutableLiveData<>(new ArrayList<>());
        saveTodoDialogFragment = new SaveTodoDialogFragment();
        binding.todoRecyclerView.addTodos(todos.getValue());

        todos.observe(getViewLifecycleOwner(), (t) -> {
            Log.d(TAG, "TODO LENGTH - " + t.size());
        });
    }

    private void listenEvents() {

        binding.todoRecyclerView.setOnTodoCheckChangedListener((todo, isChecked) -> {
            todo.setCompleted(isChecked);
            todos.setValue(todos.getValue());
            binding.todoRecyclerView.updateTodo(todo);
//            Toast.makeText(getContext(), R.string.todo_updated, Toast.LENGTH_SHORT).show();
        });

        saveTodoDialogFragment.setOnSaveTodoListener(todo -> {
            if(saveTodoDialogFragment.getTodo() == null) {
                binding.todoRecyclerView.addTodo(addTodo(todo));
//                Toast.makeText(getContext(), R.string.todo_added, Toast.LENGTH_SHORT).show();
            } else {
                binding.todoRecyclerView.updateTodo(
                        saveTodoDialogFragment.getTodo()
                );
//                Toast.makeText(getContext(), R.string.todo_updated, Toast.LENGTH_SHORT).show();
                notifyTodoUpdated();
            }
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

    private void todoDeletionRequested(Todo todo) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder.setTitle(R.string.confirmation_required)
                .setMessage(R.string.delete_todo_question)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    binding.todoRecyclerView.removeTodo(todo);
                    removeTodo(todo);
//                    Toast.makeText(getContext(), R.string.todo_deleted, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private Todo addTodo(String text) {
        final Todo todo = new Todo(text);

        addTodo(todo);

        return todo;
    }

    private void addTodo(Todo todo) {
        List<Todo> todoList = todos.getValue();

        if (todoList == null) {
            todoList = new ArrayList<>();
        }

        todoList.add(todo);

        todos.setValue(todoList);
    }

    private void removeTodo(Todo todo) {
        List<Todo> todoList = todos.getValue();

        if (todoList == null) {
            todos.setValue(new ArrayList<>());
            return;
        }

        todoList.remove(todo);
        todos.setValue(todoList);
    }

    private void notifyTodoUpdated() {
        todos.setValue(todos.getValue());
    }

    public LiveData<List<Todo>> getTodos() {
        return todos;
    }
}
