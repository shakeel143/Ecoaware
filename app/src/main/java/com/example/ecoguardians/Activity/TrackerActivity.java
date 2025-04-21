package com.example.ecoguardians.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoguardians.Adapter.LogsAdapter;
import com.example.ecoguardians.Model.LogModel;
import com.example.ecoguardians.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// TrackerActivity: This activity allows the user to track eco-friendly actions (recycling, public transport, electricity savings)
// and displays them in a list and a bar chart. Actions are logged to Firebase, and a bar chart visually represents the counts.
public class TrackerActivity extends AppCompatActivity {

    // Declare UI components for buttons, RecyclerView, and chart
    private Button btnRecycle, btnTransport, btnElectricity; // Buttons for different actions
    private RecyclerView logsRecyclerView; // RecyclerView to display logged actions
    private BarChart barChart; // Bar chart to visualize action counts
    private LogsAdapter logsAdapter; // Adapter for RecyclerView to display logs
    private List<LogModel> logsList = new ArrayList<>(); // List to store logs

    // Declare Firebase-related components
    private DatabaseReference dbRef; // Reference to Firebase Database
    private String uid; // User ID for identifying the current user in Firebase

    // Declare counters for different actions (Recycled, Public Transport, Saved Electricity)
    private int recycleCount = 0, transportCount = 0, electricityCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker); // Set the layout for the activity

        // Initialize the UI components
        ImageView imgBack = findViewById(R.id.imgBack); // Back button to navigate back to the previous activity
        imgBack.setOnClickListener(v -> finish()); // Close this activity when back button is clicked

        btnRecycle = findViewById(R.id.btnRecycle); // Button to log recycling action
        btnTransport = findViewById(R.id.btnTransport); // Button to log public transport action
        btnElectricity = findViewById(R.id.btnElectricity); // Button to log electricity savings action
        logsRecyclerView = findViewById(R.id.logsRecyclerView); // RecyclerView to display logs of eco-friendly actions
        barChart = findViewById(R.id.barChart); // Bar chart to show a visual representation of action counts

        // Get the current user's UID from FirebaseAuth
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference("SustainabilityLogs").child(uid); // Reference to store logs for the user

        // RecyclerView Setup: Set the layout manager and the adapter
        logsAdapter = new LogsAdapter(logsList);
        logsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        logsRecyclerView.setAdapter(logsAdapter);

        // Set OnClickListeners for the buttons to log actions
        btnRecycle.setOnClickListener(v -> logAction(getString(R.string.recycled))); // Recycle action
        btnTransport.setOnClickListener(v -> logAction(getString(R.string.public_transport))); // Public transport action
        btnElectricity.setOnClickListener(v -> logAction(getString(R.string.saved_electricity))); // Electricity savings action

        // Fetch the logs from Firebase to display in RecyclerView and update the chart
        fetchLogsFromFirebase();
    }

    // Method to log an action (recycle, transport, or electricity) to Firebase
    private void logAction(String action) {
        String logId = dbRef.push().getKey(); // Generate a unique ID for the log
        long timestamp = System.currentTimeMillis(); // Get the current time in milliseconds

        LogModel log = new LogModel(action, timestamp); // Create a new log object with the action and timestamp
        assert logId != null; // Ensure logId is not null before proceeding
        dbRef.child(logId).setValue(log).addOnSuccessListener(unused -> {
            // Show a Toast message when the action is successfully logged to Firebase
            Toast.makeText(this, getString(R.string.action_logged), Toast.LENGTH_SHORT).show();
        });
    }

    // Method to fetch logs from Firebase and update the RecyclerView and chart
    private void fetchLogsFromFirebase() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                logsList.clear(); // Clear the previous logs
                recycleCount = transportCount = electricityCount = 0; // Reset the counts for each action

                // Iterate through the logs fetched from Firebase
                for (DataSnapshot data : snapshot.getChildren()) {
                    LogModel log = data.getValue(LogModel.class); // Convert the data snapshot to a LogModel object
                    logsList.add(log); // Add the log to the list of logs

                    // Update the counters for each action based on the log
                    switch (log.getAction()) {
                        case "Recycled":
                            recycleCount++;
                            break;
                        case "Public Transport":
                            transportCount++;
                            break;
                        case "Saved Electricity":
                            electricityCount++;
                            break;
                    }
                }

                // Notify the adapter that the data has changed so the RecyclerView can be updated
                logsAdapter.notifyDataSetChanged();
                updateChart(); // Update the bar chart with the new data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Show a Toast message if the log retrieval fails
                Toast.makeText(TrackerActivity.this, getString(R.string.failed_to_load_logs), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to update the bar chart with the action counts
    private void updateChart() {
        // Create a list of BarEntry objects to hold the data for the bar chart
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, recycleCount)); // Bar for recycled actions
        entries.add(new BarEntry(1f, transportCount)); // Bar for public transport actions
        entries.add(new BarEntry(2f, electricityCount)); // Bar for electricity savings actions

        // Create a dataset for the bar chart
        BarDataSet dataSet = new BarDataSet(entries, getString(R.string.eco_actions)); // Set the label for the data
        dataSet.setColors(new int[]{R.color.colorPrimary, R.color.colorAccent, R.color.colorOrange}, this); // Set colors for the bars
        BarData barData = new BarData(dataSet); // Create the BarData object containing the dataset
        barData.setBarWidth(0.8f); // Set the width of the bars

        // Set the data and properties for the chart
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false); // Disable the description text for the chart
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(new String[]{getString(R.string.recycle), getString(R.string.transport), getString(R.string.electricity)})); // Set labels for the X axis
        barChart.getXAxis().setGranularity(1f); // Set granularity for X axis
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); // Position the X axis at the bottom
        barChart.animateY(1000); // Add animation to the chart
        barChart.invalidate(); // Refresh the chart to reflect the new data
    }
}
