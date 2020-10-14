package com.example.kraftnote.ui.note.editor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kraftnote.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;

public class NoteMapFragment extends Fragment {
    private static final String TAG = NoteMapFragment.class.getSimpleName();

    private GoogleMap map;
    private LatLng position;
    private String positionName;
    private SupportMapFragment mapFragment;
    private OnCloseRequest onCloseRequest;
    private MaterialButton closeButton;
    private MaterialButton navigateButton;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_note_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeProperties(view);
        listenEvents();
    }

    private void initializeProperties(View view) {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        closeButton = view.findViewById(R.id.close);
        navigateButton = view.findViewById(R.id.navigate);
    }

    private void listenEvents() {
        mapFragment.getMapAsync(this::onMapReady);
        closeButton.setOnClickListener(v -> onClose());
        navigateButton.setOnClickListener(v -> {
            startNavigateIntent();
        });
    }

    @SuppressLint("DefaultLocale")
    private void startNavigateIntent() {
        final String url = String.format("google.navigation:q=%f,%f", position.latitude, position.longitude);
        // Create a Uri from an intent string. Use the result to create an Intent.
        Uri gmmIntentUri = Uri.parse(url);

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            // Attempt to start an activity that can handle the Intent
            startActivity(mapIntent);
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        showLocation();
    }

    private void showLocation() {
        if (map != null && position == null) return;

        final MarkerOptions markerOptions = new MarkerOptions()
                .position(position)
                .title(positionName);

        map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLng(position));
        map.moveCamera(CameraUpdateFactory.zoomTo(10.5f));
    }

    public void show(@NonNull String name, @NonNull LatLng latLng) {
        position = latLng;
        positionName = name;

        showLocation();
    }

    public void setOnCloseListener(OnCloseRequest onCloseListener) {
        this.onCloseRequest = onCloseListener;
    }

    private void onClose() {
        if (onCloseRequest != null) {
            onCloseRequest.onClose();
        }
    }

    public interface OnCloseRequest {
        void onClose();
    }
}
