package com.example.kraftnote.ui.note.editor;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
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
import androidx.lifecycle.MutableLiveData;

import com.example.kraftnote.R;
import com.example.kraftnote.databinding.FragmentNoteEditorRecordingBinding;
import com.example.kraftnote.persistence.entities.NoteFile;
import com.example.kraftnote.ui.note.contracts.ViewPagerControlledFragment;
import com.example.kraftnote.utils.FileHelper;
import com.example.kraftnote.utils.PermissionHelper;
import com.visualizer.amplitude.AudioRecordView;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NoteEditorRecordingFragment extends ViewPagerControlledFragment {
    private static final String TAG = NoteEditorRecordingFragment.class.getSimpleName();

    private MediaPlayer player;
    private Timer visualizerTimer;
    private FileHelper fileHelper;
    private MediaRecorder recorder;
    private File currentRecordingSource;
    private PermissionHelper permissionHelper;
    private FragmentNoteEditorRecordingBinding binding;
    private MutableLiveData<List<NoteFile>> recordings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_editor_recording, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentNoteEditorRecordingBinding.bind(view);

        initializeProperties();
        listenEvents();
    }

    private void initializeProperties() {
        recordings = new MutableLiveData<>(new ArrayList<>());
        fileHelper = new FileHelper(requireContext().getApplicationContext());
        permissionHelper = new PermissionHelper(getContext());

        binding.recordingRecyclerView.setRecordings(recordings.getValue());
    }

    private void listenEvents() {
        binding.startButton.setOnClickListener(v -> startRecording());
        binding.stopButton.setOnClickListener(v -> stopRecording());
        binding.recordingRecyclerView.setOnDeleteClickedListener(v -> {

        });

    }

    private void startRecording() {
        if (!permissionHelper.isRecordAudioPermissionGranted()) {
            PermissionHelper.requestRecordAudioPermission(this);
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

        List<NoteFile> allRecordings = recordings.getValue() != null
                ? recordings.getValue()
                : new ArrayList<>();

        allRecordings.add(NoteFile.newAudio(currentRecordingSource.getName()));
        addRecording(NoteFile.newAudio(currentRecordingSource.getName()));

        currentRecordingSource = null;

        recordings.setValue(allRecordings);
    }

    private void startPlaying(NoteFile noteFile) {
        final File source = fileHelper.getAudioSourceFor(noteFile.getLocation());

        if (!source.exists() || !source.canRead()) {
            Toast.makeText(getContext(), R.string.audio_source_unreadable, Toast.LENGTH_SHORT).show();
            return;
        }

        player = new MediaPlayer();

        try {
            player.setDataSource(source.getAbsolutePath());
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.d(TAG, "Error playing " + source.getAbsolutePath() + " file");
            e.printStackTrace();
        }
    }

    private void addRecording(NoteFile audio) {
        List<NoteFile> recordingsValue = recordings.getValue();

        if(recordingsValue == null) {
            recordingsValue = new ArrayList<>();
        }

        recordingsValue.add(audio);

        binding.recordingRecyclerView.addRecording(audio);
    }

    private void stopPlaying() {
        if (player == null) return;

        player.stop();
        player.release();
        player = null;
    }

    @Override
    public void onStop() {
        super.onStop();

        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
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
