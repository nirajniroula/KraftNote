package com.example.kraftnote.ui.note.editor;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.kraftnote.databinding.FragmentNoteEditorImagesBinding;
import com.example.kraftnote.persistence.viewmodels.notes.editor.ImageViewModel;
import com.example.kraftnote.persistence.views.CategoryWithNotesCount;
import com.example.kraftnote.utils.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class NoteEditorImageFragment extends Fragment {
    private static final String TAG = NoteEditorImageFragment.class.getSimpleName();

    public static final int GALLERY_REQUEST = 188;
    public static final int CAMERA_REQUEST = 189;

    private View root;
    private FragmentNoteEditorImagesBinding binding;

    // data
    ImageViewModel viewModel;
    List<String> images = new ArrayList<>();

    //helper
    private FileHelper fileHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNoteEditorImagesBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeProperties();
        listenEvents();
    }

    private void initializeProperties() {
        viewModel = new ViewModelProvider(this).get(ImageViewModel.class);
        fileHelper = new FileHelper(requireContext());
    }

    private void listenEvents() {
        binding.addImageButton.setOnClickListener(v -> addImageFromGalleryRequested());
        viewModel.getImages()
                .observe(getViewLifecycleOwner(), this::imageListMutated);
    }

    private void imageListMutated(List<String> strings) {
        images = strings;

        Log.d(TAG, String.valueOf(images));
    }

    private void addImageFromGalleryRequested() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            // Attempt to start an activity that can handle the Intent
            startActivityForResult(intent, GALLERY_REQUEST);
        }
    }

    private void addImageFromCameraRequested() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            // Attempt to start an activity that can handle the Intent
            startActivityForResult(intent, CAMERA_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, String.valueOf(data));

        if (resultCode != Activity.RESULT_OK
                || data == null
        ) return;

        if (requestCode == GALLERY_REQUEST) {
            addImageFromGallery(data);
        } else if( requestCode == CAMERA_REQUEST ) {
            addImageFromCamera(data);
        }

    }

    private void addImageFromCamera(Intent data) {
        if(data.getExtras() == null) return;

        Bitmap image = (Bitmap) data.getExtras().get("data");
    }

    private void addImageFromGallery(Intent data) {
        if(data.getData() == null) return;

        try {
            InputStream imageStream = requireContext().getContentResolver().openInputStream(data.getData());
            Bitmap image = BitmapFactory.decodeStream(imageStream);
            persistToStorage(image);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void persistToStorage(Bitmap image) {
        fileHelper.saveImage(image, fileName -> {
            Log.d(TAG, fileName);
            viewModel.addImageFromBackgroundThread(fileName);
        });
    }
}
