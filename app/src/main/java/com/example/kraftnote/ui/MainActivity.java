package com.example.kraftnote.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.kraftnote.R;
import com.example.kraftnote.ui.note.editor.NoteEditorImageFragment;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private NavHostFragment navHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeProperties();
        listenEvents();
    }

    private void initializeProperties() {
        Places.initialize(this, getResources().getString(R.string.PLACES_API_KEY));
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        assert navHostFragment != null;

        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    private void listenEvents() {
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            toggleBottomNavVisibility(destination.getId() == R.id.AddUpdateNoteFragment);
        });
    }

    private void toggleBottomNavVisibility(boolean hide) {
        bottomNavigationView.setVisibility(
                !hide
                        ? View.VISIBLE
                        : View.GONE
        );
    }
}
