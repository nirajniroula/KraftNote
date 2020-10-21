package com.example.kraftnote.ui.note.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kraftnote.R;
import com.example.kraftnote.databinding.ComponentNoteCardBinding;
import com.example.kraftnote.persistence.views.NoteWithRelation;
import com.example.kraftnote.utils.DateHelper;

import java.util.ArrayList;
import java.util.List;

public class NoteRecyclerView extends RecyclerView {
    private NoteAdapter adapter;
    private OnNoteActionButtonClickedListener onChangeCategoryClickedListener;
    private OnNoteActionButtonClickedListener onEditNoteClickedListener;
    private OnNoteActionButtonClickedListener onDeleteNoteClickedListener;

    public NoteRecyclerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public NoteRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NoteRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        adapter = new NoteAdapter();

        setAdapter(adapter);
        setLayoutManager(new LinearLayoutManager(context));
        setHasFixedSize(true);

        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
                getResources().getDisplayMetrics());

        addItemDecoration(new FirstLastItemSpacesDecoration(space));
    }

    public void setNotes(List<NoteWithRelation> notes) {
        adapter.syncNotes(notes);
    }

    public void setOnChangeCategoryClickedListener(OnNoteActionButtonClickedListener onChangeCategoryClickedListener) {
        this.onChangeCategoryClickedListener = onChangeCategoryClickedListener;
    }

    public void setOnEditNoteClickedListener(OnNoteActionButtonClickedListener onEditNoteClickedListener) {
        this.onEditNoteClickedListener = onEditNoteClickedListener;
    }

    public void setOnDeleteNoteClickedListener(OnNoteActionButtonClickedListener onDeleteNoteClickedListener) {
        this.onDeleteNoteClickedListener = onDeleteNoteClickedListener;
    }

    private class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {
        private List<NoteWithRelation> notes = new ArrayList<>();

        @NonNull
        @Override
        public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.component_note_card, parent, false);

            return new NoteHolder(itemView);
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
            final NoteWithRelation note = notes.get(position);
            holder.setNoteWithRelation(note);
        }

        @Override
        public int getItemCount() {
            return notes.size();
        }

        public void syncNotes(List<NoteWithRelation> notes) {
            this.notes = notes;
            notifyDataSetChanged();
        }
    }

    private class NoteHolder extends RecyclerView.ViewHolder {
        private ComponentNoteCardBinding binding;
        private NoteWithRelation noteWithRelation;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            binding = ComponentNoteCardBinding.bind(itemView);
            inflateToolbarMenu();
        }

        private void inflateToolbarMenu() {
            final Menu menu = binding.noteToolbar.getMenu();

            menu.add(Menu.NONE, R.id.change_category, 0, R.string.change_category);
            menu.add(Menu.NONE, R.id.toolbar_edit, 1, R.string.edit);
            menu.add(Menu.NONE, R.id.toolbar_delete, 2, R.string.delete);
        }

        public void setNoteWithRelation(NoteWithRelation noteWithRelation) {
            this.noteWithRelation = noteWithRelation;
            updateUI();
        }

        private void updateUI() {
            binding.noteCreatedAt.setText(
                    DateHelper.toFormattedStringAlt(noteWithRelation.getNote().getCreatedAt()));
            binding.noteCategoryName.setText(noteWithRelation.getCategory().getName());
            binding.noteName.setText(
                    noteWithRelation.getNote().getName().trim().length() == 0
                            ? "Untitled note document"
                            : noteWithRelation.getNote().getName()
            );

            binding.noteToolbar.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.change_category:
                        if (onChangeCategoryClickedListener != null) {
                            onChangeCategoryClickedListener.onClick(noteWithRelation);
                        }
                        return true;
                    case R.id.toolbar_edit:
                        if (onEditNoteClickedListener != null) {
                            onEditNoteClickedListener.onClick(noteWithRelation);
                        }
                        return true;
                    case R.id.toolbar_delete:
                        if (onDeleteNoteClickedListener != null) {
                            onDeleteNoteClickedListener.onClick(noteWithRelation);
                        }
                        return true;
                }

                return false;
            });
        }
    }

    private static class FirstLastItemSpacesDecoration extends RecyclerView.ItemDecoration {

        private final int spaces;

        public FirstLastItemSpacesDecoration(int spaces) {
            this.spaces = spaces;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1) {
                outRect.bottom = spaces * 10;
            }
        }
    }

    public interface OnNoteActionButtonClickedListener {
        void onClick(NoteWithRelation categoryWithNotesCount);
    }
}
