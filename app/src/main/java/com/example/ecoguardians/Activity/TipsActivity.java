package com.example.ecoguardians.Activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecoguardians.Model.EcoTip;
import com.example.ecoguardians.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
// TipsActivity: This activity shows eco-friendly tips to the user, allows them to accept challenges, and tracks the status of each tip
public class TipsActivity extends AppCompatActivity {

    // Declare UI components
    private TextView tipTextView, statusTextView; // TextViews to show the tip and the status (Accepted/Not Accepted)
    private Button acceptButton, nextButton; // Buttons for accepting the tip and viewing the next tip

    // Declare data storage and randomization components
    private List<EcoTip> ecoTips; // List to store eco tips
    private EcoTip currentTip; // Holds the current tip being displayed
    private SharedPreferences prefs; // SharedPreferences to store tip acceptance status

    private Random random; // Random object to generate random tip index
    private int lastTipIndex = -1; // Variable to keep track of the last shown tip index to avoid repetition

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips); // Set the layout for the activity

        // Initialize the UI components
        ImageView imgBack = findViewById(R.id.imgBack); // Back button to exit the activity
        imgBack.setOnClickListener(v -> finish()); // When clicked, finish the current activity and go back
        tipTextView = findViewById(R.id.tipText); // TextView to display the tip
        statusTextView = findViewById(R.id.statusText); // TextView to display the status of the challenge
        acceptButton = findViewById(R.id.acceptBtn); // Button to accept the tip challenge
        nextButton = findViewById(R.id.nextTipBtn); // Button to view the next tip

        // Initialize SharedPreferences and Random
        prefs = getSharedPreferences("EcoPrefs", MODE_PRIVATE); // Store user's accepted tips
        random = new Random(); // Random object to select random tips

        // Load tips and show a random tip initially
        loadTips();
        showRandomTip();

        // Set onClickListener for the accept button
        acceptButton.setOnClickListener(v -> {
            // Mark the current tip as accepted in SharedPreferences
            prefs.edit().putBoolean(currentTip.tipText, true).apply();
            updateStatus(true); // Update status to "Accepted"
            acceptButton.setEnabled(false); // Disable the accept button once accepted
            acceptButton.setText("✅ Accepted"); // Change text to "Accepted"
        });

        // Set onClickListener for the next button to show the next random tip
        nextButton.setOnClickListener(v -> {
            showRandomTip(); // Show a new random tip when next button is clicked
        });
    }

    // Method to load predefined eco tips into the list
    private void loadTips() {
        ecoTips = new ArrayList<>(); // Initialize the list of eco tips

        // Add different eco tips to the list (titles and actions)
        ecoTips.add(new EcoTip(getString(R.string.tip1_title), getString(R.string.tip1_action)));
        ecoTips.add(new EcoTip(getString(R.string.tip2_title), getString(R.string.tip2_action)));
        ecoTips.add(new EcoTip(getString(R.string.tip3_title), getString(R.string.tip3_action)));
        ecoTips.add(new EcoTip(getString(R.string.tip4_title), getString(R.string.tip4_action)));
        ecoTips.add(new EcoTip(getString(R.string.tip5_title), getString(R.string.tip5_action)));
        ecoTips.add(new EcoTip(getString(R.string.tip6_title), getString(R.string.tip6_action)));
        ecoTips.add(new EcoTip(getString(R.string.tip7_title), getString(R.string.tip7_action)));
        ecoTips.add(new EcoTip(getString(R.string.tip8_title), getString(R.string.tip8_action)));
        ecoTips.add(new EcoTip(getString(R.string.tip9_title), getString(R.string.tip9_action)));
        ecoTips.add(new EcoTip(getString(R.string.tip10_title), getString(R.string.tip10_action)));
    }

    // Method to update the status TextView based on whether the tip was accepted or not
    private void updateStatus(boolean accepted) {
        if (accepted) {
            // If accepted, show the green status with "Challenge Accepted!"
            statusTextView.setText("✅ Challenge Accepted!");
            statusTextView.setBackgroundColor(Color.parseColor("#43A047")); // Green color for accepted
        } else {
            // If not accepted, show the gray status with "Not Accepted"
            statusTextView.setText("❌ Not Accepted");
            statusTextView.setBackgroundColor(Color.parseColor("#B0BEC5")); // Gray color for not accepted
        }
    }

    // Method to display a random eco tip to the user
    private void showRandomTip() {
        int index;
        do {
            // Generate a random index that is different from the last displayed tip
            index = random.nextInt(ecoTips.size());
        } while (index == lastTipIndex && ecoTips.size() > 1); // Ensure the new index is different from the previous one

        lastTipIndex = index; // Update the last tip index
        currentTip = ecoTips.get(index); // Set the current tip to the randomly selected tip

        // Display the tip's title and challenge action
        tipTextView.setText(currentTip.tipText + "\n\n👉 " + currentTip.challenge);

        // Check if the current tip has been accepted (stored in SharedPreferences)
        boolean isAccepted = prefs.getBoolean(currentTip.tipText, false);
        updateStatus(isAccepted); // Update the status display

        // If the tip is accepted, disable the accept button and show "Accepted" text
        if (isAccepted) {
            acceptButton.setEnabled(false); // Disable the button
            acceptButton.setText("✅ Accepted"); // Change button text to "Accepted"
        } else {
            acceptButton.setEnabled(true); // Enable the button for unaccepted tips
            acceptButton.setText("✅ Accept Challenge"); // Set the default button text
        }
    }
}
