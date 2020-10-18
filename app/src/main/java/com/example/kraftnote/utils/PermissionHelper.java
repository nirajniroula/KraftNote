package com.example.kraftnote.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;

public class PermissionHelper {
    private final WeakReference<Context> contextReference;

    // permission code
    public static final int RECORD_AUDIO_PERMISSION_CODE = 661;
    public static final int WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 717;

    // permissions
    private static final String[] RECORD_AUDIO = new String[]{Manifest.permission.RECORD_AUDIO};
    private static final String[] WRITE_EXTERNAL_STORAGE = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public PermissionHelper(Context context) {
        this.contextReference = new WeakReference<>(context);
    }

    public boolean isIntentResolvable(Intent intent) {
        if(contextReference.get() == null) return false;

        return intent.resolveActivity(contextReference.get().getPackageManager()) != null;
    }

    public boolean isRecordAudioPermissionGranted() {
        if (contextReference.get() == null) return false;

        return PackageManager.PERMISSION_GRANTED
                ==
                contextReference.get().checkSelfPermission(RECORD_AUDIO[0]);
    }

    public static void requestRecordAudioPermission(Fragment fragment) {
        if (fragment == null) return;
        fragment.requestPermissions(RECORD_AUDIO, RECORD_AUDIO_PERMISSION_CODE);
    }

    public boolean isWriteExternalStoragePermissionGranted() {
        if (contextReference.get() == null) return false;

        return PackageManager.PERMISSION_GRANTED
                ==
                contextReference.get().checkSelfPermission(WRITE_EXTERNAL_STORAGE[0]);
    }

    public static void requestWriteExternalStoragePermission(Fragment fragment) {
        if (fragment == null) return;
        fragment.requestPermissions(WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);
    }
}
