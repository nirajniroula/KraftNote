package com.example.kraftnote.ui.note.editor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.ViewModelProvider;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.kraftnote.R;
import com.example.kraftnote.databinding.FragmentNoteEditorImagesBinding;
import com.example.kraftnote.persistence.entities.Note;
import com.example.kraftnote.persistence.entities.NoteFile;
import com.example.kraftnote.persistence.viewmodels.NoteFileViewModel;
import com.example.kraftnote.ui.note.contracts.NoteEditorChildBaseFragment;
import com.example.kraftnote.utils.FileHelper;
import com.example.kraftnote.utils.PermissionHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class NoteEditorImageFragment extends NoteEditorChildBaseFragment {
    private static final String TAG = NoteEditorImageFragment.class.getSimpleName();

    public static final int GALLERY_REQUEST = 188;

    private NoteFileViewModel noteFileViewModel;

    private View root;
    private FragmentNoteEditorImagesBinding binding;

    //views
    ImageAdapter imageAdapter;

    //helper
    private FileHelper fileHelper;
    private PermissionHelper permissionHelper;

    // state
    private File capturedImage;
    private boolean allowViewPagerSwipeGesture = true;
    private NoteFile currentOpenedImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        noteFileViewModel = new ViewModelProvider(this).get(NoteFileViewModel.class);
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
        imageAdapter = new ImageAdapter();
        fileHelper = new FileHelper(requireContext());
        permissionHelper = new PermissionHelper(getContext());

        binding.imageGridView.setAdapter(imageAdapter);

        binding.imageViewer
                .setBackgroundResource(R.color.semi_transparent_black);
    }

    private void listenEvents() {
        noteFileViewModel.getAll().observe(getViewLifecycleOwner(), this::imageListMutated);

        binding.closeImageViewerButton.setOnClickListener(v -> closeImageViewer());
        binding.deleteImageButton.setOnClickListener(v -> onImageDeleteRequest());
        binding.addImageButton.setOnClickListener(v -> addImageFromGalleryRequested());
    }

    private void onImageDeleteRequest() {
        if (currentOpenedImage == null) return;

        final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder.setTitle(R.string.confirmation_required)
                .setMessage(R.string.delete_image_question)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    noteFileViewModel.delete(currentOpenedImage);
                    fileHelper.deleteImage(currentOpenedImage);
                    Toast.makeText(getContext(), R.string.image_deleted, Toast.LENGTH_SHORT).show();
                    closeImageViewer();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void closeImageViewer() {
        currentOpenedImage = null;
        binding.imageViewerWrapper.setVisibility(View.GONE);
        allowViewPagerSwipeGesture = true;
        updateViewPagerScrollBehaviour(true);
    }

    private void imageListMutated(List<NoteFile> files) {
        Note note = getNote();

        if(note == null) return;

        imageAdapter.syncImages(
                files.stream()
                        .filter(NoteFile::isImage)
                        .filter(noteFile -> Objects.equals(note.getId(), noteFile.getNoteId()))
                        .collect(Collectors.toCollection(ArrayList<NoteFile>::new))
        );

        Log.d(TAG, String.valueOf(files));
    }

    private void addImageFromGalleryRequested() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Log.d(TAG, intent.toString());

        if (permissionHelper.isIntentResolvable(intent)) {
            // Attempt to start an activity that can handle the Intent
            startActivityForResult(intent, GALLERY_REQUEST);
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
        }
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
        WeakReference<Context> contextWeakReference = new WeakReference<>(getContext());
        WeakReference<View> viewWeakReference = new WeakReference<>(binding.getRoot());
        binding.progressBarWrapper.setZ(1);
        binding.progressBarWrapper.setClickable(true);
        binding.progressBarWrapper.setVisibility(View.VISIBLE);

        if(getNote() == null) return;

        fileHelper.saveImage(image, fileName -> {
            Log.d(TAG, fileName);

            if (viewWeakReference.get() == null) return;

            noteFileViewModel.insert(NoteFile.newImage(fileName, getNote().getId()));

            viewWeakReference.get().post(() -> {
                binding.progressBarWrapper.setVisibility(View.GONE);

                if (contextWeakReference.get() != null) {
                    Toast.makeText(contextWeakReference.get(), R.string.image_added, Toast.LENGTH_SHORT).show();
                }

            });
        });
    }

    private class SquareImageView extends AppCompatImageView {
        private NoteFile imageFile;
        private Bitmap bitmap;

        public SquareImageView(Context context) {
            super(context);
            init();
        }

        public SquareImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init();
        }

        private void setImageFile(NoteFile file) {
            this.imageFile = file;
            bitmap = fileHelper.readImage(file.getLocation());
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
        }

        private void init() {
            setOnClickListener(v -> {
                if (bitmap == null) return;
                Bitmap bitmapClone = bitmap.copy(bitmap.getConfig(), true);

                currentOpenedImage = imageFile;
                binding.imageViewer.setImage(ImageSource.bitmap(bitmapClone));
                binding.imageViewer.resetScaleAndCenter();
                binding.imageViewerWrapper.setVisibility(View.VISIBLE);
                allowViewPagerSwipeGesture = false;
                updateViewPagerScrollBehaviour(false);
            });
        }
    }

    @Override
    public void onFragmentVisible() {
        super.onFragmentVisible();

        updateViewPagerScrollBehaviour(allowViewPagerSwipeGesture);
    }

    private class ImageAdapter extends BaseAdapter {
        private List<NoteFile> images = new ArrayList<>();
        private Map<String, Bitmap> bitmaps = new HashMap<>();

        public void syncImages(List<NoteFile> files) {
            images = files;

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
            SquareImageView imageView = new SquareImageView(requireContext());
            imageView.setImageFile(file);

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
