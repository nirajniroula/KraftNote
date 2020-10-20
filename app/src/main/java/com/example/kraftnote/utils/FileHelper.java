package com.example.kraftnote.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class FileHelper {
    public static final String IMAGE_DIRECTORY_NAME = "images";
    public static final String AUDIO_DIRECTORY_NAME = "audios";

    private final File imageDirectory;
    private final File audioDirectory;
    private final File cacheDirectory;

    public FileHelper(Context context) {
        ContextWrapper wrapper = new ContextWrapper(context.getApplicationContext());

        imageDirectory = wrapper.getDir(IMAGE_DIRECTORY_NAME, Context.MODE_PRIVATE);
        audioDirectory = wrapper.getDir(AUDIO_DIRECTORY_NAME, Context.MODE_PRIVATE);
        cacheDirectory = wrapper.getCacheDir();
    }

    public File getAudioOutputFile() {
        return new File(getAudioDirectory(), makeAudioName());
    }

    public File getAudioSourceFor(String location) {
        return new File(getAudioDirectory(), location);
    }

    public File getImageDirectory() {
        return imageDirectory;
    }

    public File getAudioDirectory() {
        return audioDirectory;
    }

    public File getAudioDirectoryFile(String location) {
        return new File(getAudioDirectory(), location);
    }

    public void saveImage(Bitmap bitmap, Consumer<String> onComplete) {
        String filename = System.currentTimeMillis() + ".png";
        File file = new File(imageDirectory, filename);
        bitmap = resizeBitmap(bitmap, 1000, 1000);

        Bitmap finalBitmap = bitmap;

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                FileOutputStream out = new FileOutputStream(file);
                finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();

                onComplete.accept(filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public Bitmap readImage(final String fileName) {
        File file = new File(imageDirectory, fileName);

        try {
            InputStream inputStream = new FileInputStream(file);
            Bitmap image = BitmapFactory.decodeStream(inputStream);

            inputStream.close();

            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap resizeBitmap(Bitmap source, int width, int height) {
        if (source.getHeight() == height && source.getWidth() == width) return source;
        int maxLength = Math.min(width, height);
        try {
            source = source.copy(source.getConfig(), true);
            if (source.getHeight() <= source.getWidth()) {
                if (source.getHeight() <= maxLength) { // if image already smaller than the required height
                    return source;
                }

                double aspectRatio = (double) source.getWidth() / (double) source.getHeight();
                int targetWidth = (int) (maxLength * aspectRatio);

                return Bitmap.createScaledBitmap(source, targetWidth, maxLength, false);
            } else {

                if (source.getWidth() <= maxLength) { // if image already smaller than the required height
                    return source;
                }

                double aspectRatio = ((double) source.getHeight()) / ((double) source.getWidth());
                int targetHeight = (int) (maxLength * aspectRatio);

                return Bitmap.createScaledBitmap(source, maxLength, targetHeight, false);

            }
        } catch (Exception e) {
            return source;
        }
    }

    public static Long makeName() {
        return System.currentTimeMillis();
    }

    public static String makeImageName() {
        return makeName() + ".png";
    }

    public static String makeAudioName() {
        return makeName() + ".mp3";
    }
}
