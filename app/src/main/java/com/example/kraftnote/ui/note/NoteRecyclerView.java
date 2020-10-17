package com.example.kraftnote.ui.note;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.icu.text.SimpleDateFormat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kraftnote.R;
import com.example.kraftnote.databinding.ComponentNoteCardBinding;
import com.example.kraftnote.persistence.views.NoteWithRelation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NoteRecyclerView extends RecyclerView {
    private NoteAdapter adapter;
    private List<NoteWithRelation> notes;

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
        this.notes = notes;
        adapter.syncNotes(notes);
    }



    private static class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {
        private List<NoteWithRelation> notes = new ArrayList<>();
        private final String datePattern = "EEE, d MMM yyyy, hh:mm a";
        private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern, Locale.US);

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

    private static class NoteHolder extends RecyclerView.ViewHolder {
        private ComponentNoteCardBinding binding;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            binding = ComponentNoteCardBinding.bind(itemView);
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
