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

public class TipsActivity extends AppCompatActivity {

    private TextView tipTextView, statusTextView;
    private Button acceptButton, nextButton;

    private List<EcoTip> ecoTips;
    private EcoTip currentTip;
    private SharedPreferences prefs;

    private Random random;
    private int lastTipIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> finish());
        tipTextView = findViewById(R.id.tipText);
        statusTextView = findViewById(R.id.statusText);
        acceptButton = findViewById(R.id.acceptBtn);
        nextButton = findViewById(R.id.nextTipBtn);

        prefs = getSharedPreferences("EcoPrefs", MODE_PRIVATE);
        random = new Random();

        loadTips();
        showRandomTip();

        acceptButton.setOnClickListener(v -> {
            prefs.edit().putBoolean(currentTip.tipText, true).apply();
            updateStatus(true);
            acceptButton.setEnabled(false);
            acceptButton.setText("✅ Accepted");
        });

        nextButton.setOnClickListener(v -> {
            showRandomTip();
        });
    }

    private void loadTips() {
        ecoTips = new ArrayList<>();

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

    private void updateStatus(boolean accepted) {
        if (accepted) {
            statusTextView.setText("✅ Challenge Accepted!");
            statusTextView.setBackgroundColor(Color.parseColor("#43A047")); // green
        } else {
            statusTextView.setText("❌ Not Accepted");
            statusTextView.setBackgroundColor(Color.parseColor("#B0BEC5")); // gray
        }
    }

    private void showRandomTip() {
        int index;
        do {
            index = random.nextInt(ecoTips.size());
        } while (index == lastTipIndex && ecoTips.size() > 1);

        lastTipIndex = index;
        currentTip = ecoTips.get(index);

        // Fix: changed actionText to challenge
        tipTextView.setText(currentTip.tipText + "\n\n👉 " + currentTip.challenge);

        boolean isAccepted = prefs.getBoolean(currentTip.tipText, false);
        updateStatus(isAccepted);

        if (isAccepted) {
            acceptButton.setEnabled(false);
            acceptButton.setText("✅ Accepted");
        } else {
            acceptButton.setEnabled(true);
            acceptButton.setText("✅ Accept Challenge");
        }
    }

}
