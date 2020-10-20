package com.example.kraftnote.ui.note.editor;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.kraftnote.R;
import com.example.kraftnote.databinding.FragmentNoteEditorRemindersBinding;
import com.example.kraftnote.persistence.entities.DatetimeReminder;
import com.example.kraftnote.persistence.entities.LocationReminder;
import com.example.kraftnote.persistence.transformers.PlaceToLocationReminder;
import com.example.kraftnote.ui.note.contracts.ViewPagerFragment;
import com.example.kraftnote.utils.DateHelper;
import com.example.kraftnote.utils.LocationHelper;
import com.example.kraftnote.utils.PermissionHelper;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.Date;

public class NoteEditorReminderFragment extends ViewPagerFragment {
    private static final String TAG = NoteEditorReminderFragment.class.getSimpleName();

    private FragmentNoteEditorRemindersBinding binding;

    // data
    private MutableLiveData<LocationReminder> locationReminder;
    private MutableLiveData<DatetimeReminder> datetimeReminder;
    private GoogleMap googleMap;

    //child fragments
    private SupportMapFragment supportMapFragment;
    private AutocompleteSupportFragment autocompleteSupportFragment;

    //helper
    private PermissionHelper permissionHelper;

    // picker
    private MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().build();

    // state
    private boolean allowViewPagerSwipeGesture = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_editor_reminders, container, false);
        binding = FragmentNoteEditorRemindersBinding.bind(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeProperties();
        listenEvents();
    }

    private void initializeProperties() {
        permissionHelper = new PermissionHelper(getContext());
        locationReminder = new MutableLiveData<>();
        datetimeReminder = new MutableLiveData<>();

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        autocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.places_autocomplete_fragment);

        assert autocompleteSupportFragment != null;
        assert autocompleteSupportFragment.getView() != null;

        autocompleteSupportFragment.getView().setBackgroundColor(Color.WHITE);
        LocationHelper.forFragment(autocompleteSupportFragment);
    }

    private void listenEvents() {
        binding.showMapButton.setOnClickListener(v -> openGoogleMaps());
        binding.closeMapButton.setOnClickListener(v -> closeGoogleMap());

        binding.navigateButton.setOnClickListener(v -> dispatchMapNavigationIntent());
        binding.dateTimePickerButton.setOnClickListener(v -> showDatetimePicker());
        supportMapFragment.getMapAsync(this::onMapReady);

        autocompleteSupportFragment.setOnPlaceSelectedListener(placeSelectionListener);
        datePicker.addOnPositiveButtonClickListener(onDateSelectionListener);

        datetimeReminder.observe(getViewLifecycleOwner(), reminder -> onDatetimeSelected());
        locationReminder.observe(getViewLifecycleOwner(), reminder -> onLocationSelected());
    }

    private void onDatetimeSelected() {
        String text;

        if (datetimeReminder.getValue() == null) {
            text = getResources().getString(R.string.select_a_date_and_time);
        } else {
            text = DateHelper.toFormattedString(datetimeReminder.getValue().getDatetime());
        }

        binding.selectedDateTextView.setText(text);
    }

    private void onLocationSelected() {
        if (googleMap == null || locationReminder.getValue() == null) {
            binding.selectedLocationTextView.setText(R.string.select_a_location);
            return;
        }

        String fullAddress = locationReminder.getValue().getFullAddress();
        String locationName = locationReminder.getValue().getName();
        LatLng position = locationReminder.getValue().getLatLng();

        binding.selectedLocationTextView.setText(fullAddress);

        googleMap.clear();

        final MarkerOptions markerOptions = new MarkerOptions()
                .position(position)
                .title(locationName);

        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(12.5f));
    }

    private void showDatetimePicker() {
        datePicker.show(getChildFragmentManager(), datePicker.toString());
    }

    private void openGoogleMaps() {
        if (locationReminder.getValue() == null || locationReminder.getValue().getLatLng() == null) {
            Toast.makeText(getContext(), R.string.select_a_location, Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "MAPS OPEN");

        if (googleMap == null) return;

        binding.googleMapCardView.setVisibility(View.VISIBLE);

        allowViewPagerSwipeGesture = false;
        updateViewPagerScrollBehaviour(false);
    }

    private void closeGoogleMap() {
        binding.googleMapCardView.setVisibility(View.GONE);
        Log.d(TAG, "MAPS CLOSE");

        allowViewPagerSwipeGesture = true;
        updateViewPagerScrollBehaviour(true);
    }

    @SuppressLint("DefaultLocale")
    private void dispatchMapNavigationIntent() {
        if (locationReminder.getValue() == null) return;

        LatLng position = locationReminder.getValue().getLatLng();

        final String url = String.format("google.navigation:q=%f,%f", position.latitude, position.longitude);

        Uri intentUri = Uri.parse(url);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");

        if (permissionHelper.isIntentResolvable(mapIntent)) {
            // Attempt to start an activity that can handle the Intent
            requireContext().startActivity(mapIntent);
        }
    }

    private void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        onLocationSelected();
    }

    private PlaceSelectionListener placeSelectionListener = new PlaceSelectionListener() {
        @Override
        public void onPlaceSelected(@NonNull Place place) {
            LocationReminder reminder = PlaceToLocationReminder.make(place);
            locationReminder.setValue(reminder);
        }

        @Override
        public void onError(@NonNull Status status) {
            Log.d(TAG, String.valueOf(status));
        }
    };

    private MaterialPickerOnPositiveButtonClickListener<Long> onDateSelectionListener = new MaterialPickerOnPositiveButtonClickListener<Long>() {
        @Override
        public void onPositiveButtonClick(Long timestamp) {

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    getContext(), R.style.Theme_MyTheme_Dialog,

                    (picker, hour, minute) -> {
                        Date date = DateHelper.timestampToDate(timestamp, hour, minute);
                        datetimeReminder.setValue(new DatetimeReminder(date));
                    },

                    DateHelper.getCurrentHour(),
                    DateHelper.getCurrentMinute(),
                    false
            );

            timePickerDialog.show();
        }
    };

    @Override
    public void onFragmentVisible() {
        super.onFragmentVisible();

        updateViewPagerScrollBehaviour(allowViewPagerSwipeGesture);
    }
}
