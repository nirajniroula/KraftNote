package com.example.kraftnote.ui.note.editor;

import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.kraftnote.R;
import com.example.kraftnote.databinding.FragmentNoteEditorRecordingBinding;
import com.example.kraftnote.persistence.entities.NoteFile;
import com.example.kraftnote.persistence.viewmodels.NoteFileViewModel;
import com.example.kraftnote.ui.note.contracts.NoteEditorChildBaseFragment;
import com.example.kraftnote.utils.FileHelper;
import com.example.kraftnote.utils.PermissionHelper;
import com.visualizer.amplitude.AudioRecordView;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class NoteEditorRecordingFragment extends NoteEditorChildBaseFragment {
    private static final String TAG = NoteEditorRecordingFragment.class.getSimpleName();

    private Timer visualizerTimer;
    private FileHelper fileHelper;
    private MediaRecorder recorder;
    private File currentRecordingSource;
    private PermissionHelper permissionHelper;
    private FragmentNoteEditorRecordingBinding binding;
    private List<NoteFile> recordings = new ArrayList<>();
    private NoteFileViewModel noteFileViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_editor_recording, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noteFileViewModel = new ViewModelProvider(this).get(NoteFileViewModel.class);
        binding = FragmentNoteEditorRecordingBinding.bind(view);

        initializeProperties();
        listenEvents();
    }

    private void initializeProperties() {
        fileHelper = new FileHelper(requireContext().getApplicationContext());
        permissionHelper = new PermissionHelper(getContext());
    }

    private void listenEvents() {
        noteFileViewModel.getAll().observe(getViewLifecycleOwner(), this::noteFileMutated);
        binding.startButton.setOnClickListener(v -> startRecording());
        binding.stopButton.setOnClickListener(v -> stopRecording());
        binding.recordingRecyclerView.setOnDeleteClickedListener(this::onDeleteRecordingRequest);
    }

    private void noteFileMutated(List<NoteFile> noteFiles) {
        recordings = noteFiles.stream()
                .filter(NoteFile::isAudio)
                .filter(noteFile -> Objects.equals(getNote().getId(), noteFile.getNoteId()))
                .collect(Collectors.toCollection(ArrayList<NoteFile>::new));

        binding.recordingRecyclerView.setRecordings(recordings);
    }

    public void deleteRecording(NoteFile recording) {
        Log.d(TAG, "deleteRecording: " + recording.toString());
        noteFileViewModel.delete(recording);
        fileHelper.deleteAudio(recording);
        Toast.makeText(getContext(), R.string.recording_deleted, Toast.LENGTH_SHORT).show();
    }

    public void onDeleteRecordingRequest(NoteFile recoding) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder.setTitle(R.string.confirmation_required)
                .setMessage(R.string.delete_recording_question)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    binding.recordingRecyclerView.removeRecording(recoding);
                    deleteRecording(recoding);
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void startRecording() {
        if (!permissionHelper.isRecordAudioPermissionGranted()) {
            PermissionHelper.requestRecordAudioPermission(this);
            return;
        }

        binding.startRecordingHint.setVisibility(View.INVISIBLE);
        binding.startButton.setEnabled(false);
        binding.stopButton.setEnabled(true);

        if (recorder != null) return;

        final WeakReference<AudioRecordView> recordViewWeakReference = new WeakReference<>(binding.visualizer);
        final WeakReference<MediaRecorder> recorderWeakReference = new WeakReference<>(recorder);

        visualizerTimer = new Timer();

        visualizerTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (recordViewWeakReference.get() == null) return;

                recordViewWeakReference.get().post(() -> {
                    if (recordViewWeakReference.get() == null || recorderWeakReference.get() == null)
                        return;

                    recordViewWeakReference.get().update(recorderWeakReference.get().getMaxAmplitude());
                });
            }
        }, 0, 250);

        binding.chronometer.setBase(SystemClock.elapsedRealtime());
        binding.chronometer.start();
        binding.visualizer.recreate();

        binding.chronometer.setOnChronometerTickListener(chronometer -> {
            if (recorder != null)
                binding.visualizer.update(recorder.getMaxAmplitude());
        });

        recorder = new MediaRecorder();
        currentRecordingSource = fileHelper.getAudioOutputFile();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setOutputFile(currentRecordingSource);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.d(TAG, "Prepare Failed");
            e.printStackTrace();
        }

        recorder.start();
    }

    private void stopRecording() {
        binding.startButton.setEnabled(true);
        binding.stopButton.setEnabled(false);
        binding.chronometer.stop();
        binding.chronometer.setOnChronometerTickListener(null);
        binding.startRecordingHint.setVisibility(View.VISIBLE);

        if (recorder == null) return;

        visualizerTimer.cancel();
        visualizerTimer = null;

        recorder.stop();
        recorder.reset();
        recorder.release();
        recorder = null;

        addRecording(NoteFile.newAudio(currentRecordingSource.getName(), getNote().getId()));

        currentRecordingSource = null;
    }

    private void addRecording(NoteFile audio) {
        binding.recordingRecyclerView.addRecording(audio);
        audio.setId(noteFileViewModel.insertSingle(audio));
    }

    @Override
    public void onStop() {
        super.onStop();

        if (recorder != null) {
            recorder.reset();
            recorder.release();
            recorder = null;
        }

        if (visualizerTimer != null) {
            visualizerTimer.cancel();
            visualizerTimer = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionHelper.RECORD_AUDIO_PERMISSION_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), R.string.permission_not_granted, Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            startRecording();
        }
    }
}
