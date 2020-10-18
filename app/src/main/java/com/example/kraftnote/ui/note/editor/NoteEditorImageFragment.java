package com.example.kraftnote.ui.note.editor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.kraftnote.R;
import com.example.kraftnote.databinding.FragmentNoteEditorImagesBinding;
import com.example.kraftnote.persistence.entities.NoteFile;
import com.example.kraftnote.utils.DateHelper;
import com.example.kraftnote.utils.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteEditorImageFragment extends Fragment {
    private static final String TAG = NoteEditorImageFragment.class.getSimpleName();

    public static final int GALLERY_REQUEST = 188;
    public static final int CAMERA_REQUEST = 189;

    private View root;
    private FragmentNoteEditorImagesBinding binding;

    //views
    ImageAdapter imageAdapter;

    // data
    private MutableLiveData<List<NoteFile>> images;

    //helper
    private FileHelper fileHelper;

    //temp data
    File capturedImage;

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
        images = new MutableLiveData<>(new ArrayList<>());
        imageAdapter = new ImageAdapter();
        fileHelper = new FileHelper(requireContext());

        binding.imageGridView.setAdapter(imageAdapter);
    }

    private void listenEvents() {
        binding.addImageButton.setOnClickListener(v -> addImageFromGalleryRequested());
        binding.addImageCameraButton.setOnClickListener(v -> addImageFromCameraRequested());
        images.observe(getViewLifecycleOwner(), this::imageListMutated);
    }

    public void setImages(List<NoteFile> images) {
        this.images.setValue(images);
    }

    public LiveData<List<NoteFile>> getImages() {
        return images;
    }

    private void imageListMutated(List<NoteFile> files) {
//        imageRecyclerView.setImages(images.getValue());
        imageAdapter.syncImages(files);

        Log.d(TAG, String.valueOf(files));
    }

    private void addImageFromGalleryRequested() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Log.d(TAG, intent.toString());

        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            // Attempt to start an activity that can handle the Intent
            startActivityForResult(intent, GALLERY_REQUEST);
        }
    }

    private void addImageFromCameraRequested() {
        int granted = requireContext().checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (granted != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        }

        try {
            capturedImage = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Uri imageUri = FileProvider.getUriForFile(requireContext(),
                "com.example.kraftnote.fileProvider",
                capturedImage);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImage);

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
        } else if (requestCode == CAMERA_REQUEST) {
            addImageFromCamera(data);
        }

    }

    private void addImageFromCamera(Intent data) {
        if (capturedImage == null) return;

        Bitmap image = (Bitmap) BitmapFactory.decodeFile(capturedImage.getAbsolutePath());
        persistToStorage(image);
    }

    private void addImageFromGallery(Intent data) {
        if (data.getData() == null) return;

        try {
            InputStream imageStream = requireContext().getContentResolver().openInputStream(data.getData());
            Bitmap image = BitmapFactory.decodeStream(imageStream);
            persistToStorage(image);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, Arrays.toString(grantResults));
    }

    private void persistToStorage(Bitmap image) {
        fileHelper.saveImage(image, fileName -> {
            Log.d(TAG, fileName);

            List<NoteFile> currentImages = images.getValue();

            if (currentImages == null) {
                currentImages = new ArrayList<>();
            }

            currentImages.add(NoteFile.newImage(fileName));

            images.postValue(currentImages);
        });
    }

    private File createImageFile() throws IOException {
        String fileName = DateHelper.toFileNameFormat("KraftNote_", "_");
        File storageDirectory = requireContext().getFilesDir();

        Log.d(TAG, storageDirectory.getAbsolutePath());
        Log.d(TAG, storageDirectory.getCanonicalPath());

        File image = File.createTempFile(
                fileName,
                ".png",
                storageDirectory
        );

        String mCurrentPhotoPath = image.getAbsolutePath();

        Log.d(TAG, "Path: " + mCurrentPhotoPath);

        return image;
    }

    public class SquareImageView extends AppCompatImageView {
        public SquareImageView(Context context) {
            super(context);
        }

        public SquareImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private List<NoteFile> images = new ArrayList<>();
        private Map<String, Bitmap> bitmaps = new HashMap<>();

        public void syncImages(List<NoteFile> files) {
            images = new ArrayList<>();

            for (NoteFile file : files) {
                if (!file.isImage()) continue;

                images.add(file);
            }

            // latest to the top
            Collections.reverse(images);

            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int position) {
            return images.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NoteFile file = (NoteFile) getItem(position);
            ImageView imageView = new SquareImageView(requireContext());

            int dimen = imageView.getMeasuredWidth();
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            if (!bitmaps.containsKey(file.getLocation())) {
                Bitmap bitmap = fileHelper.readImage(file.getLocation());
                bitmap = fileHelper.resizeBitmap(bitmap, dimen, dimen);
                bitmaps.put(file.getLocation(), bitmap);
            }

            imageView.setImageBitmap(bitmaps.get(file.getLocation()));

            return imageView;
        }
    }
}
