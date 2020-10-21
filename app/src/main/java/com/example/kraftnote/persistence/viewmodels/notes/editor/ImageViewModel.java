package com.example.kraftnote.persistence.viewmodels.notes.editor;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class ImageViewModel extends AndroidViewModel {
    private MutableLiveData<List<String>> mutableLiveDataImages = new MutableLiveData<>();

    public ImageViewModel(@NonNull Application application) {
        super(application);
    }

    public void addImage(String image) {
        List<String> images = new ArrayList<>();
        images.add(image);

        if(mutableLiveDataImages.getValue() != null)
            images.addAll(mutableLiveDataImages.getValue());

        mutableLiveDataImages.setValue(images);
    }

    public void addImages(List<String> images) {
        if(mutableLiveDataImages.getValue() != null)
            images.addAll(mutableLiveDataImages.getValue());

        mutableLiveDataImages.setValue(images);
    }

    public LiveData<List<String>> getImages() {
        return mutableLiveDataImages;
    }

    public void addImageFromBackgroundThread(String image) {
        List<String> images = new ArrayList<>();
        images.add(image);

        if(mutableLiveDataImages.getValue() != null)
            images.addAll(mutableLiveDataImages.getValue());

        mutableLiveDataImages.postValue(images);
    }
}
