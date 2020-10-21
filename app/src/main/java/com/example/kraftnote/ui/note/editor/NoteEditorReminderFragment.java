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
import androidx.lifecycle.ViewModelProvider;

import com.example.kraftnote.R;
import com.example.kraftnote.databinding.FragmentNoteEditorRemindersBinding;
import com.example.kraftnote.persistence.entities.DatetimeReminder;
import com.example.kraftnote.persistence.entities.LocationReminder;
import com.example.kraftnote.persistence.transformers.PlaceToLocationReminder;
import com.example.kraftnote.persistence.viewmodels.DatetimeReminderViewModel;
import com.example.kraftnote.persistence.viewmodels.LocationReminderViewModel;
import com.example.kraftnote.ui.note.contracts.NoteEditorChildFragmentBase;
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

public class NoteEditorReminderFragment extends NoteEditorChildFragmentBase {
    private static final String TAG = NoteEditorReminderFragment.class.getSimpleName();

    private FragmentNoteEditorRemindersBinding binding;

    // data
    private LocationReminder locationReminder;
    private DatetimeReminder datetimeReminder;
    private GoogleMap googleMap;

    //child fragments
    private SupportMapFragment supportMapFragment;
    private AutocompleteSupportFragment autocompleteSupportFragment;

    //helper
    private PermissionHelper permissionHelper;

    // view model
    private DatetimeReminderViewModel datetimeReminderViewModel;
    private LocationReminderViewModel locationReminderViewModel;

    // picker
    private MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().build();

    // state
    private boolean allowViewPagerSwipeGesture = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        datetimeReminderViewModel = new ViewModelProvider(this).get(DatetimeReminderViewModel.class);
        locationReminderViewModel = new ViewModelProvider(this).get(LocationReminderViewModel.class);

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
        locationReminder = locationReminderViewModel.findByNoteId(getNote().getId());
        datetimeReminder = datetimeReminderViewModel.findByNoteId(getNote().getId());

        permissionHelper = new PermissionHelper(getContext());

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        autocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.places_autocomplete_fragment);

        assert autocompleteSupportFragment != null;
        assert autocompleteSupportFragment.getView() != null;

        autocompleteSupportFragment.getView().setBackgroundColor(Color.WHITE);
        LocationHelper.forFragment(autocompleteSupportFragment);

        onDatetimeSelected();
        onLocationSelected();
    }

    private void listenEvents() {
        binding.showMapButton.setOnClickListener(v -> openGoogleMaps());
        binding.closeMapButton.setOnClickListener(v -> closeGoogleMap());

        binding.navigateButton.setOnClickListener(v -> dispatchMapNavigationIntent());
        binding.dateTimePickerButton.setOnClickListener(v -> showDatetimePicker());
        supportMapFragment.getMapAsync(this::onMapReady);

        autocompleteSupportFragment.setOnPlaceSelectedListener(placeSelectionListener);
        datePicker.addOnPositiveButtonClickListener(onDateSelectionListener);
    }

    private void onDatetimeSelected() {
        String text;

        if (datetimeReminder == null) {
            text = getResources().getString(R.string.select_a_date_and_time);
        } else {
            text = DateHelper.toFormattedString(datetimeReminder.getDatetime());
        }

        binding.selectedDateTextView.setText(text);
    }

    private void onLocationSelected() {
        if(locationReminder == null) {
            binding.selectedLocationTextView.setText(R.string.select_a_location);
            return;
        }

        String fullAddress = locationReminder.getFullAddress();
        binding.selectedLocationTextView.setText(fullAddress);

        if (googleMap == null ) return;

        String locationName = locationReminder.getName();
        LatLng position = locationReminder.getLatLng();

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
        if (locationReminder == null || locationReminder.getLatLng() == null) {
            Toast.makeText(getContext(), R.string.select_a_location, Toast.LENGTH_SHORT).show();
            return;
        }

        onLocationSelected();

        if (googleMap == null) return;

        binding.googleMapCardView.setVisibility(View.VISIBLE);

        allowViewPagerSwipeGesture = false;
        updateViewPagerScrollBehaviour(false);
    }

    private void closeGoogleMap() {
        binding.googleMapCardView.setVisibility(View.GONE);

        allowViewPagerSwipeGesture = true;
        updateViewPagerScrollBehaviour(true);
    }

    @SuppressLint("DefaultLocale")
    private void dispatchMapNavigationIntent() {
        if (locationReminder == null) return;

        LatLng position = locationReminder.getLatLng();

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
            LocationReminder reminder = PlaceToLocationReminder.make(place, getNote().getId());

            if (locationReminder == null) {
                reminder.setId(locationReminderViewModel.insertSingle(reminder));

                Toast.makeText(getContext(), R.string.location_reminder_set, Toast.LENGTH_SHORT)
                        .show();

                locationReminder = reminder;

            } else {

                reminder.setId(locationReminder.getId());
                locationReminderViewModel.update(reminder);
                locationReminder = reminder;

                Toast.makeText(getContext(), R.string.location_reminder_updated, Toast.LENGTH_SHORT)
                        .show();
            }

            onLocationSelected();
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

                        if (datetimeReminder == null) {
                            datetimeReminder = new DatetimeReminder(date);
                            datetimeReminder.setNoteId(getNote().getId());
                            datetimeReminder.setId(datetimeReminderViewModel.insertSingle(datetimeReminder));

                            Toast.makeText(getContext(), R.string.datetime_reminder_set, Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            datetimeReminder.setNoteId(getNote().getId());
                            datetimeReminder.setDatetime(date);
                            datetimeReminderViewModel.update(datetimeReminder);

                            Toast.makeText(getContext(), R.string.datetime_reminder_updated, Toast.LENGTH_SHORT)
                                    .show();
                        }

                        onDatetimeSelected();
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
