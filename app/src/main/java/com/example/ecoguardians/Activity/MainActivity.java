package com.example.ecoguardians.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView welcomeText;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private List<DashboardItem> dashboardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        welcomeText = findViewById(R.id.welcomeText);
        recyclerView = findViewById(R.id.dashboardRecycler);

        mAuth = FirebaseAuth.getInstance();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // User is not logged in, navigate to LoginActivity
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish(); // Finish MainActivity so the user can't go back without logging in
            return; // Important: Exit onCreate to prevent further execution
        }

        dbRef = FirebaseDatabase.getInstance().getReference("Users");

        setupDashboard();
        loadUserData();
    }

    private void setupDashboard() {
        dashboardList = new ArrayList<>();
        dashboardList.add(new DashboardItem(getString(R.string.dashboard_education), R.drawable.ic_education));
        dashboardList.add(new DashboardItem(getString(R.string.dashboard_tips_challenges), R.drawable.ic_challenge));
        dashboardList.add(new DashboardItem(getString(R.string.dashboard_tracker), R.drawable.ic_tracker));
        dashboardList.add(new DashboardItem(getString(R.string.dashboard_community), R.drawable.ic_community));
        dashboardList.add(new DashboardItem(getString(R.string.dashboard_resources), R.drawable.ic_resources));
        dashboardList.add(new DashboardItem(getString(R.string.dashboard_settings), R.drawable.ic_settings));

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(new DashboardAdapter(this, dashboardList));
    }

    private void loadUserData() {
        String uid = mAuth.getCurrentUser().getUid();
        dbRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String language = SettingsActivity.getSelectedLanguageString(MainActivity.this);
                welcomeText.setText(getString(R.string.welcome) + ", " + name + "(" + language + ")");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                welcomeText.setText(getString(R.string.welcome));
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        SharedPreferences prefs = base.getSharedPreferences("language_pref", MODE_PRIVATE);
        String lang = prefs.getString("selected_lang", "en");
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = base.getResources().getConfiguration();
        config.setLocale(locale);
        super.attachBaseContext(base.createConfigurationContext(config));
    }

}
