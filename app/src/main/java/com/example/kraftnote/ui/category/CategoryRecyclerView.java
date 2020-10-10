package com.example.kraftnote.ui.category;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kraftnote.R;
import com.example.kraftnote.persistence.entities.Category;
import com.example.kraftnote.persistence.views.CategoryWithNotesCount;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class CategoryRecyclerView extends RecyclerView {
    private List<CategoryWithNotesCount> categories = new ArrayList<>();
    private final Random rand = new Random();
    private OnCategoryActionButtonClickedListener onEditClickListener;
    private OnCategoryActionButtonClickedListener onDeleteClickListener;
    private CategoryAdapter adapter;

    public CategoryRecyclerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CategoryRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CategoryRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        adapter = new CategoryAdapter();

        setAdapter(adapter);
        setLayoutManager(new LinearLayoutManager(context));
        setHasFixedSize(true);


    }

    public void setCategories(List<CategoryWithNotesCount> categories) {
        this.categories = categories;
        adapter.syncCategories(categories);
    }

    public void onEditButtonClicked(OnCategoryActionButtonClickedListener listener) {
        onEditClickListener = listener;
    }

    public void onDeleteButtonClicked(OnCategoryActionButtonClickedListener listener) {
        onDeleteClickListener = listener;
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
        private List<CategoryWithNotesCount> categoryWithNotesCounts = new ArrayList<>();
        private final String datePattern = "EEE, d MMM yyyy, hh:mm a";
        private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern, Locale.US);

        @NonNull
        @Override
        public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_card, parent, false);

            return new CategoryAdapter.CategoryHolder(itemView);
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
            final CategoryWithNotesCount categoryWithNotesCount = categoryWithNotesCounts.get(position);
            final Category category = categoryWithNotesCount.getCategory();
            final int notesCount = categoryWithNotesCount.getNotesCount();

            String formattedDate = simpleDateFormat.format(category.getCreatedAt());
            holder.textViewName.setText(category.getName());

            holder.textViewNotesCount.setText(
                    String.format(
                            "%d Note%s",
                            notesCount,
                            (notesCount > 1) ? "s" : ""
                    )
            );

            holder.textViewCreatedAt.setText(formattedDate);

            holder.toolbar.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.category_toolbar_edit:
                        if(onEditClickListener != null)
                            onEditClickListener.onClick(categoryWithNotesCount);
                        return true;
                    case R.id.category_toolbar_delete:
                        if(onDeleteClickListener != null)
                            onDeleteClickListener.onClick(categoryWithNotesCount);
                        return true;
                    default:
                        return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return categoryWithNotesCounts.size();
        }

        public void syncCategories(List<CategoryWithNotesCount> categoryWithNotesCounts) {
            this.categoryWithNotesCounts = categoryWithNotesCounts;
            notifyDataSetChanged();
        }

        private class CategoryHolder extends RecyclerView.ViewHolder {
            private final TextView textViewName;
            private final TextView textViewNotesCount;
            private final TextView textViewCreatedAt;
            private final MaterialToolbar toolbar;

            public CategoryHolder(@NonNull View itemView) {
                super(itemView);
                textViewName = itemView.findViewById(R.id.category_name);
                textViewNotesCount = itemView.findViewById(R.id.category_notes_count);
                textViewCreatedAt = itemView.findViewById(R.id.category_created_at);
                toolbar = itemView.findViewById(R.id.category_toolbar);
                toolbar.inflateMenu(R.menu.category_card_popup_menu);
            }
        }
    }

    public interface OnCategoryActionButtonClickedListener {
        void onClick(CategoryWithNotesCount categoryWithNotesCount);
    }
}
