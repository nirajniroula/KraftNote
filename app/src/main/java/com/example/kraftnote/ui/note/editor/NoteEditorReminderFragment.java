package com.example.kraftnote.ui.note.editor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.kraftnote.databinding.FragmentNoteEditorRemindersBinding;
import com.example.kraftnote.persistence.entities.LocationReminder;
import com.example.kraftnote.persistence.transformers.PlaceToLocationReminder;
import com.example.kraftnote.utils.LocationHelper;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

public class NoteEditorReminderFragment extends Fragment {
    private static final String TAG = NoteEditorReminderFragment.class.getSimpleName();

    private FragmentNoteEditorRemindersBinding binding;

    // data
    private LocationReminder locationReminder;
    private LatLng position;
    private GoogleMap googleMap;

    //child fragments
    private SupportMapFragment supportMapFragment;
    private AutocompleteSupportFragment autocompleteSupportFragment;

    // listeners
    private OnLocationChangedListener onLocationChangedListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_editor_reminders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentNoteEditorRemindersBinding.bind(view);

        initializeProperties();
        listenEvents();
    }

    private void initializeProperties() {
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        autocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.places_autocomplete_fragment);

        if (autocompleteSupportFragment != null && autocompleteSupportFragment.getView() != null) {
            autocompleteSupportFragment.getView().setBackgroundColor(Color.WHITE);
        }

        Log.d(TAG, String.valueOf(supportMapFragment));
        Log.d(TAG, String.valueOf(autocompleteSupportFragment));

        LocationHelper.forFragment(autocompleteSupportFragment);
    }

    private void listenEvents() {
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                LocationReminder locationReminder = PlaceToLocationReminder.make(place);
                locationReminderUpdated(locationReminder);
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.d(TAG, String.valueOf(status));
            }
        });

        binding.closeMapButton.setOnClickListener(v -> closeGoogleMap());
        binding.navigateButton.setOnClickListener(v -> startNavigateIntent());
        binding.locationViewerButton.setOnClickListener(v -> openGoogleMaps());

        supportMapFragment.getMapAsync(this::onMapReady);
    }

    private void openGoogleMaps() {
        if (googleMap == null || position == null || locationReminder == null) return;
        binding.googleMapCardView.setVisibility(View.VISIBLE);
    }

    public void setLocationReminder(LocationReminder locationReminder) {
        this.locationReminder = locationReminder;

        if (locationReminder == null) {
            binding.selectedLocationTextView.setText(R.string.select_a_location);
            return;
        }

        position = locationReminder.getLatLng();
        binding.selectedLocationTextView.setText(locationReminder.getFullAddress());

        updateGoogleMapCamera();
    }

    private void locationReminderUpdated(LocationReminder locationReminder) {
        if (onLocationChangedListener != null) {
            onLocationChangedListener.onLocationChanged(locationReminder);
        }

        setLocationReminder(locationReminder);
    }

    @SuppressLint("DefaultLocale")
    private void startNavigateIntent() {
        if (position == null) return;

        final String url = String.format("google.navigation:q=%f,%f", position.latitude, position.longitude);
        // Create a Uri from an intent string. Use the result to create an Intent.
        Uri gmmIntentUri = Uri.parse(url);

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Attempt to start an activity that can handle the Intent
            getContext().startActivity(mapIntent);
        }
    }

    private void updateGoogleMapCamera() {
        if (googleMap == null || position == null || locationReminder == null) return;

        final MarkerOptions markerOptions = new MarkerOptions()
                .position(position)
                .title(locationReminder.getName());

        googleMap.clear();
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(10.5f));
    }

    private void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        updateGoogleMapCamera();
    }

    private void closeGoogleMap() {
        binding.googleMapCardView.setVisibility(View.INVISIBLE);
    }

    public void setOnLocationChangedListener(OnLocationChangedListener onLocationChangedListener) {
        this.onLocationChangedListener = onLocationChangedListener;
    }

    public interface OnLocationChangedListener {
        void onLocationChanged(LocationReminder locationReminder);
    }
}
