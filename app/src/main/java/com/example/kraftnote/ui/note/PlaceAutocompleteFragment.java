package com.example.kraftnote.ui.note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class PlaceAutocompleteFragment extends AutocompleteSupportFragment {
    private OnPlaceSelected onPlaceSelected;
    private OnPlaceSelectionError onPlaceSelectionError;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));

        setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                if(onPlaceSelected != null) {
                    onPlaceSelected.onSelected(place);
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                if(onPlaceSelectionError != null) {
                    onPlaceSelectionError.onError(status);
                }
            }
        });
    }

    public void setOnPlaceSelected(OnPlaceSelected onPlaceSelected) {
        this.onPlaceSelected = onPlaceSelected;
    }

    public void setOnPlaceSelectionError(OnPlaceSelectionError onPlaceSelectionError) {
        this.onPlaceSelectionError = onPlaceSelectionError;
    }


    public interface OnPlaceSelected {
        void onSelected(Place place);
    }

    public interface OnPlaceSelectionError {
        void onError(Status status);
    }
}
