package com.example.kraftnote.ui.note.editor.components;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kraftnote.R;
import com.example.kraftnote.databinding.ComponentRecordingItemBinding;
import com.example.kraftnote.persistence.entities.NoteFile;
import com.example.kraftnote.utils.DateHelper;
import com.example.kraftnote.utils.FileHelper;

import java.util.ArrayList;
import java.util.List;

public class RecordingRecyclerView extends RecyclerView {
    private FileHelper fileHelper;
    private RecordingAdapter adapter;
    private OnComponentItemClickListener onEditClicked;
    private OnComponentItemClickListener onDeleteClicked;
    private OnComponentItemClickListener onPlayClicked;
    private List<NoteFile> recordings = new ArrayList<>();

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

    public void setRecordings(List<NoteFile> recordings) {
        this.recordings = new ArrayList<>(recordings);
        adapter.notifyDataSetChanged();
    }

    public void addRecording(NoteFile recording) {
        post(() -> {
            recordings.add(recording);
            adapter.notifyItemInserted(adapter.getItemCount() - 1);
        });
    }

    public void removeRecording(NoteFile recording) {
        int idx = recordings.indexOf(recording);

        if (idx < 0) return;

        post(() -> {
            recordings.remove(recording);
            adapter.notifyItemRemoved(idx);
        });
    }

    public void updateRecording(NoteFile recording) {
        int idx = recordings.indexOf(recording);

        if (idx < 0) return;

        post(() -> {
            recordings.set(idx, recording);
            adapter.notifyItemChanged(idx);
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
            Log.d("NoteEditor Recording Size", "" + recordings.size());
            return recordings.size();
        }


    }

    private class RecordingHolder extends RecyclerView.ViewHolder {
        private ComponentRecordingItemBinding binding;
        private NoteFile recording;

        public RecordingHolder(@NonNull View itemView) {
            super(itemView);
            binding = ComponentRecordingItemBinding.bind(itemView);
            binding.toolbar.inflateMenu(R.menu.generic_item_menu);

            listenEvents();
        }

        private void listenEvents() {
            Log.d("NoteEditor", "initialized");
            binding.toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
        }

        public void setRecording(NoteFile recording) {
            this.recording = recording;
            binding.createdAt.setText(DateHelper.toFormattedString(recording.getCreatedAt()));
            binding.audioPlayer.setAudioTarget(
                    fileHelper.getAudioDirectoryFile(recording.getLocation()).getAbsolutePath());
        }

        private Toolbar.OnMenuItemClickListener onMenuItemClickListener = item -> {
            switch (item.getItemId()) {
                case R.id.toolbar_edit:
                    if (onEditClicked != null) {
                        onEditClicked.onClick(recording);
                    }
                    break;
                case R.id.toolbar_delete:
                    if (onDeleteClicked != null) {
                        onDeleteClicked.onClick(recording);
                    }
                    break;
            }

            return true;
        };
    }

    public interface OnComponentItemClickListener {
        void onClick(NoteFile recording);
    }
}
