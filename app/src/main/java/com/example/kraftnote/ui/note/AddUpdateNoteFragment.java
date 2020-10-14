package com.example.kraftnote.ui.note;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.kraftnote.R;
import com.example.kraftnote.persistence.entities.Category;
import com.example.kraftnote.persistence.entities.LocationReminder;
import com.example.kraftnote.persistence.entities.Note;
import com.example.kraftnote.persistence.transformers.PlaceToLocationReminder;
import com.example.kraftnote.persistence.viewmodels.CategoryViewModel;
import com.example.kraftnote.ui.note.editor.NoteBodyText;
import com.example.kraftnote.ui.note.editor.NoteMapFragment;
import com.example.kraftnote.utils.LocationHelper;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class AddUpdateNoteFragment extends Fragment {
    private static final String TAG = AddUpdateNoteFragment.class.getSimpleName();
    private static final int AUTOCOMPLETE_REQUEST_CODE = 67;

    private NavController navController;
    private CategoryViewModel categoryViewModel;
    private MaterialButton closeEditorButton;
    private MaterialButton saveNoteButton;
    private List<Category> categories = new ArrayList<>();
    private LinearLayout bottomSheet;
    private NoteBodyText noteBodyText;
    private NoteMapFragment noteMapFragment;

    // places
    private MaterialCardView placesCardView;
    private AutocompleteSupportFragment autocompleteFragment;
    private MaterialButton locationButton;

    //meta
    private TextView locationReminderTextView;
    private TextView datetimeReminderTextView;

    // state
    private Note note;
    private LocationReminder locationReminder;

    // This callback will only be called when AddUpdateNoteFragment is at least started
    private
    OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            AddUpdateNoteFragment.this.gotoNoteFragment();
        }
    };

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);


        requireActivity().getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), onBackPressedCallback);

        return inflater.inflate(R.layout.fragment_add_update_note, container, false);
    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeProperties(view);
        listenEvents(view);
    }

    private void initializeProperties(@NonNull final View view) {
        note = new Note();
        navController = NavHostFragment.findNavController(this);
        closeEditorButton = view.findViewById(R.id.close_editor_button);
        noteBodyText = view.findViewById(R.id.editor_body_text);
        saveNoteButton = view.findViewById(R.id.save_note_button);
        bottomSheet = view.findViewById(R.id.note_editor_bottom_sheet);
        locationButton = view.findViewById(R.id.bottom_sheet_location);
        placesCardView = view.findViewById(R.id.places_autosuggest_cardview);
        locationReminderTextView = view.findViewById(R.id.location_reminder_detail);
        datetimeReminderTextView = view.findViewById(R.id.datetime_reminder_detail);

        noteMapFragment = (NoteMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.show_google_map_fragment);

        autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager()
                .findFragmentById(R.id.places_autocomplete_fragment);

        assert autocompleteFragment != null;
        LocationHelper.forFragment(autocompleteFragment);
    }

    private void listenEvents(@NonNull final View view) {
        categoryViewModel.getAll().observe(getViewLifecycleOwner(), this::categoriesMutated);
        closeEditorButton.setOnClickListener(v -> gotoNoteFragment());

        locationButton.setOnClickListener(v -> {
            placesCardView.setVisibility(View.VISIBLE);
        });

        locationReminderTextView.setOnClickListener(v -> {
            if(locationReminder == null) return;

            view.findViewById(R.id.map_material_card_view).setVisibility(View.VISIBLE);
            noteMapFragment.show(locationReminder.getName(), locationReminder.getLatLng());
        });

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                setLocationReminder(place);

                Log.d(TAG, place.toString());
                placesCardView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.d(TAG, String.valueOf(status));
                placesCardView.setVisibility(View.INVISIBLE);
            }
        });

        noteMapFragment.setOnCloseListener(() -> {
            view.findViewById(R.id.map_material_card_view).setVisibility(View.GONE);
        });
    }

    private void setLocationReminder(Place place) {
        locationReminder = PlaceToLocationReminder.make(place);
        locationReminderTextView.setText(locationReminder.getFullAddress());
        locationReminderTextView.setVisibility(View.VISIBLE);
    }

    private void categoriesMutated(List<Category> categories) {
        this.categories = categories;
    }

    private void gotoNoteFragment() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder
                .setTitle(R.string.confirmation_required)
                .setMessage(R.string.do_you_want_to_discard_the_changes)
                .setCancelable(false)
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    navController.navigate(R.id.action_AddUpdateNoteFragment_to_NoteFragment);
                })
                .show();
    }
}
