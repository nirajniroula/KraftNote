package com.example.kraftnote.ui.category;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kraftnote.R;
import com.example.kraftnote.persistence.entities.Category;
import com.example.kraftnote.persistence.views.CategoryWithNotesCount;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private List<CategoryWithNotesCount> categories = new ArrayList<>();
    private final Random rand = new Random();
    private final String datePattern = "EEEE, d MMMM yyyy, hh:mm a";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern, Locale.US);
    private OnActionButtonClickedListener onActionButtonClickedListener;

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_card, parent, false);

        return new CategoryHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        final CategoryWithNotesCount categoryWithNotesCount = categories.get(position);
        final Category category = categoryWithNotesCount.category;
        final int notesCount = categoryWithNotesCount.notesCount;

        int count = rand.nextInt(10);
        String formattedDate = simpleDateFormat.format(category.getCreatedAt());
        holder.textViewName.setText(categoryWithNotesCount.category.getName());
        holder.textViewNotesCount.setText(notesCount + (
                categoryWithNotesCount.notesCount > 1 ? " Notes" : " Note"
        ));
        holder.textViewCreatedAt.setText(formattedDate);

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onActionButtonClickedListener != null)
                    onActionButtonClickedListener.onClick(categoryWithNotesCount, ButtonType.EDIT);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onActionButtonClickedListener != null)
                    onActionButtonClickedListener.onClick(categoryWithNotesCount, ButtonType.DELETE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<CategoryWithNotesCount> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public void setOnActionButtonClickedListener(OnActionButtonClickedListener onActionButtonClickedListener) {
        this.onActionButtonClickedListener = onActionButtonClickedListener;
    }

    static class CategoryHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewNotesCount;
        private TextView textViewCreatedAt;

        private Button editButton;
        private Button deleteButton;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.category_name);
            textViewNotesCount = itemView.findViewById(R.id.category_notes_count);
            textViewCreatedAt = itemView.findViewById(R.id.category_created_at);
            editButton = itemView.findViewById(R.id.category_edit_button);
            deleteButton = itemView.findViewById(R.id.category_delete_button);
        }
    }

    public enum ButtonType {
        EDIT,
        DELETE
    }

    public static interface OnActionButtonClickedListener {
        void onClick(CategoryWithNotesCount categoryWithNotesCount, ButtonType buttonType);
    }
}
