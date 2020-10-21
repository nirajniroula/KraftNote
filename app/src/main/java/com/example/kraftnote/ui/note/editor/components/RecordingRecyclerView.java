package com.example.kraftnote.ui.note.editor.components;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kraftnote.R;
import com.example.kraftnote.databinding.ComponentRecordingItemBinding;
import com.example.kraftnote.persistence.entities.NoteFile;
import com.example.kraftnote.utils.DateHelper;
import com.example.kraftnote.utils.FileHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecordingRecyclerView extends RecyclerView {
    private FileHelper fileHelper;
    private RecordingAdapter adapter;
    private OnComponentItemClickListener onDeleteClicked;

    private List<NoteFile> recordings;

    public RecordingRecyclerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public RecordingRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecordingRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        adapter = new RecordingAdapter();
        fileHelper = new FileHelper(context);

        setAdapter(adapter);
        setLayoutManager(new LinearLayoutManager(context));
        setHasFixedSize(true);
    }

    public void setOnDeleteClickedListener(OnComponentItemClickListener onDeleteClicked) {
        this.onDeleteClicked = onDeleteClicked;
    }

    public void setRecordings(List<NoteFile> recordings) {
        if(this.recordings != null) return;

        this.recordings = recordings;

        adapter.notifyDataSetChanged();
    }

    public void addRecording(NoteFile recording) {
        if (recordings == null) {
            recordings = new ArrayList<>();
        }

        post(() -> {
            recordings.add(recording);
            adapter.notifyItemInserted(adapter.getItemCount() - 1);
        });
    }

    public void removeRecording(NoteFile recording) {
        if (recordings == null) return;

        int idx = recordings.indexOf(recording);

        if (idx < 0) return;

        post(() -> {
            recordings.remove(recording);
            adapter.notifyItemRemoved(idx);
        });
    }

    private class RecordingAdapter extends RecyclerView.Adapter<RecordingHolder> {
        @NonNull
        @Override
        public RecordingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.component_recording_item, parent, false);

            return new RecordingHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecordingHolder holder, int position) {
            holder.setRecording(recordings.get(position));
        }

        @Override
        public int getItemCount() {
            if (recordings == null) return 0;

            return recordings.size();
        }
    }

    private class RecordingHolder extends RecyclerView.ViewHolder {
        private ComponentRecordingItemBinding binding;
        private NoteFile recording;


        public RecordingHolder(@NonNull View itemView) {
            super(itemView);
            binding = ComponentRecordingItemBinding.bind(itemView);

            listenEvents();
        }

        private void listenEvents() {
            Log.d("NoteEditor", "initialized");
            binding.deleteButton.setOnClickListener(v -> {
                if (onDeleteClicked != null) {
                    onDeleteClicked.onClick(recording);
                }
            });
        }

        public void setRecording(NoteFile recording) {
            this.recording = recording;
            binding.createdAt.setText(DateHelper.toFormattedString(recording.getCreatedAt()));
            binding.audioPlayer.setAudioTarget(
                    fileHelper.getAudioDirectoryFile(recording.getLocation()).getAbsolutePath());
        }
    }

    public interface OnComponentItemClickListener {
        void onClick(NoteFile recording);
    }
}
