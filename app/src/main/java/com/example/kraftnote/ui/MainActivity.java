package com.example.kraftnote.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.kraftnote.R;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
