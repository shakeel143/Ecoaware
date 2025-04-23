package com.example.ecoguardians.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoguardians.Adapter.DashboardAdapter;
import com.example.ecoguardians.Model.DashboardItem;
import com.example.ecoguardians.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// MainActivity is the main dashboard screen that appears after user logs in
public class MainActivity extends AppCompatActivity {

    // UI elements
    private RecyclerView recyclerView; // RecyclerView to display dashboard items
    private TextView welcomeText; // TextView to show a welcome message with the user's name and language
    private ImageView ivLogout;
    // Firebase instances
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    // List to store dashboard items
    private List<DashboardItem> dashboardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout for this activity
        setContentView(R.layout.activity_main2);

        // Initialize views
        ivLogout = findViewById(R.id.ivLogout);
        welcomeText = findViewById(R.id.welcomeText);
        recyclerView = findViewById(R.id.dashboardRecycler);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        ivLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        // Check if the user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // If not logged in, redirect to LoginActivity
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish(); // Prevent user from going back to this activity
            return; // Exit onCreate
        }

        // Get reference to the "Users" node in Firebase Realtime Database
        dbRef = FirebaseDatabase.getInstance().getReference("Users");

        // Setup the dashboard UI and load user data
        setupDashboard();
        loadUserData();
    }

    // This method initializes the dashboard items and sets up the RecyclerView
    private void setupDashboard() {
        dashboardList = new ArrayList<>();

        // Add all dashboard sections (with name and icon)
        dashboardList.add(new DashboardItem(getString(R.string.dashboard_education), R.drawable.ic_education));
        dashboardList.add(new DashboardItem(getString(R.string.dashboard_tips_challenges), R.drawable.ic_challenge));
        dashboardList.add(new DashboardItem(getString(R.string.dashboard_tracker), R.drawable.ic_tracker));
        dashboardList.add(new DashboardItem(getString(R.string.dashboard_community), R.drawable.ic_community));
        dashboardList.add(new DashboardItem(getString(R.string.dashboard_resources), R.drawable.ic_resources));
        dashboardList.add(new DashboardItem(getString(R.string.dashboard_settings), R.drawable.ic_settings));

        // Set RecyclerView layout to a grid with 2 columns
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Attach adapter to the RecyclerView
        recyclerView.setAdapter(new DashboardAdapter(this, dashboardList));
    }

    // Loads the user's name from Firebase and sets a welcome message
    private void loadUserData() {
        String uid = mAuth.getCurrentUser().getUid(); // Get current user's UID
        dbRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Retrieve the user's name
                String name = snapshot.child("name").getValue(String.class);

                // Get the selected language (from shared preferences or settings)
                String language = SettingsActivity.getSelectedLanguageString(MainActivity.this);

                // Set the welcome message with the name and language
                welcomeText.setText(getString(R.string.welcome) + ", " + name + " (" + language + ")");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // In case of failure, show generic welcome text
                welcomeText.setText(getString(R.string.welcome));
            }
        });
    }

    // This method sets the app's language based on saved preference
    @Override
    protected void attachBaseContext(Context base) {
        // Get selected language from shared preferences
        SharedPreferences prefs = base.getSharedPreferences("language_pref", MODE_PRIVATE);
        String lang = prefs.getString("selected_lang", "en");

        // Set the locale
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = base.getResources().getConfiguration();
        config.setLocale(locale);

        // Apply the new configuration
        super.attachBaseContext(base.createConfigurationContext(config));
    }
}
