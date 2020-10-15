package com.example.kraftnote.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    public FileHelper(Context context) {
        ContextWrapper wrapper = new ContextWrapper(context.getApplicationContext());

        imageDirectory = wrapper.getDir(IMAGE_DIRECTORY_NAME, Context.MODE_PRIVATE);
        audioDirectory = wrapper.getDir(AUDIO_DIRECTORY_NAME, Context.MODE_PRIVATE);
    }

    public void saveImage(Bitmap bitmap, Consumer<String> onComplete) {
        String filename = System.currentTimeMillis() + ".png";
        File file = new File(imageDirectory, filename);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
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
}
