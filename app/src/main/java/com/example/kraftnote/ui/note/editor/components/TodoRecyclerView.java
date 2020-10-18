package com.example.kraftnote.ui.note.editor.components;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kraftnote.R;
import com.example.kraftnote.databinding.ComponentTodoListItemBinding;
import com.example.kraftnote.persistence.entities.Todo;

import java.util.ArrayList;
import java.util.List;

public class TodoRecyclerView extends RecyclerView {
    private TodoAdapter adapter;
    private OnTodoCheckChangedListener onTodoCheckChangedListener;
    private OnToolBarMenuItemClicked onToolBarMenuEditClicked;
    private OnToolBarMenuItemClicked onToolBarMenuDeleteClicked;

    public TodoRecyclerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public TodoRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TodoRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        adapter = new TodoAdapter();

        setAdapter(adapter);
        setLayoutManager(new LinearLayoutManager(context));
        setHasFixedSize(false);
    }

    public void addTodos(List<Todo> todoList) {
        if (todoList != null) {
            adapter.syncTodos(todoList);
        }
    }

    public void addTodo(Todo todo) {
        adapter.addTodo(todo);
    }

    public void updateTodo(Todo todo) {
        adapter.updateTodo(todo);
    }

    public void removeTodo(Todo todo) {
        adapter.removeTodo(todo);
    }

    public void setOnTodoCheckChangedListener(OnTodoCheckChangedListener listener) {
        this.onTodoCheckChangedListener = listener;
    }

    public void setOnToolBarMenuDeleteClicked(OnToolBarMenuItemClicked onToolBarMenuDeleteClicked) {
        this.onToolBarMenuDeleteClicked = onToolBarMenuDeleteClicked;
    }

    public void setOnToolBarMenuEditClicked(OnToolBarMenuItemClicked onToolBarMenuEditClicked) {
        this.onToolBarMenuEditClicked = onToolBarMenuEditClicked;
    }

    private class TodoAdapter extends RecyclerView.Adapter<TodoHolder> {
        private List<Todo> todoList = new ArrayList<>();

        @NonNull
        @Override
        public TodoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.component_todo_list_item, parent, false);

            return new TodoHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull TodoHolder holder, int position) {
            holder.setTodo(todoList.get(position));
        }

        @Override
        public int getItemCount() {
            return todoList.size();
        }

        public void syncTodos(List<Todo> todoList) {
            this.todoList = new ArrayList<>(todoList);

            post(this::notifyDataSetChanged);
        }

        public void addTodo(Todo todo) {
            todoList.add(todo);
            post(() -> notifyItemInserted(todoList.size() - 1));
        }

        public void removeTodo(Todo todo) {
            int idx = todoList.indexOf(todo);
            todoList.remove(todo);
            if (idx == -1) return;

            post(() -> notifyItemRemoved(idx));
        }

        public void updateTodo(Todo todo) {
            int idx = todoList.indexOf(todo);

            if (idx == -1) return;

            todoList.set(idx, todo);

            post(() -> notifyItemChanged(idx));
        }
    }

    private class TodoHolder extends RecyclerView.ViewHolder {
        private ComponentTodoListItemBinding binding;
        private Todo todo;
        private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = (buttonView, isChecked) -> {
            if (onTodoCheckChangedListener != null) {
                onTodoCheckChangedListener.onCheckChanged(todo, isChecked);
            }
        };

        private Toolbar.OnMenuItemClickListener onMenuItemClickListener = item -> {
            switch (item.getItemId()) {
                case R.id.todo_toolbar_edit:
                    if (onToolBarMenuEditClicked != null) {
                        onToolBarMenuEditClicked.onClicked(todo);
                    }
                    break;
                case R.id.todo_toolbar_delete:
                    if (onToolBarMenuDeleteClicked != null) {
                        onToolBarMenuDeleteClicked.onClicked(todo);
                    }
                    break;
            }

            return true;
        };

        public TodoHolder(@NonNull View itemView) {
            super(itemView);
            binding = ComponentTodoListItemBinding.bind(itemView);
            listenEvents();
        }

        private void listenEvents() {
            binding.todoToolbar.inflateMenu(R.menu.todo_item_menu);
            binding.todoCheckbox.setOnCheckedChangeListener(onCheckedChangeListener);
            binding.todoToolbar.setOnMenuItemClickListener(onMenuItemClickListener);
        }

        public void setTodo(Todo todo) {
            this.todo = todo;
            binding.todoTask.setText(todo.getTask());

            if(todo.isCompleted()) {
                binding.todoTask.setPaintFlags(binding.todoTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            binding.todoCheckbox.setOnCheckedChangeListener(null);
            binding.todoCheckbox.setChecked(todo.isCompleted());
            binding.todoCheckbox.setOnCheckedChangeListener(onCheckedChangeListener);
        }
    }

    public interface OnTodoCheckChangedListener {
        void onCheckChanged(Todo todo, boolean isChecked);
    }

    public interface OnToolBarMenuItemClicked {
        void onClicked(Todo todo);
    }
}
