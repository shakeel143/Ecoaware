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

public class TrackerActivity extends AppCompatActivity {

    private Button btnRecycle, btnTransport, btnElectricity;
    private RecyclerView logsRecyclerView;
    private BarChart barChart;
    private LogsAdapter logsAdapter;
    private List<LogModel> logsList = new ArrayList<>();

    private DatabaseReference dbRef;
    private String uid;

    private int recycleCount = 0, transportCount = 0, electricityCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> finish());
        btnRecycle = findViewById(R.id.btnRecycle);
        btnTransport = findViewById(R.id.btnTransport);
        btnElectricity = findViewById(R.id.btnElectricity);
        logsRecyclerView = findViewById(R.id.logsRecyclerView);
        barChart = findViewById(R.id.barChart);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference("SustainabilityLogs").child(uid);

        // RecyclerView Setup
        logsAdapter = new LogsAdapter(logsList);
        logsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        logsRecyclerView.setAdapter(logsAdapter);

        // Button Listeners
        btnRecycle.setOnClickListener(v -> logAction(getString(R.string.recycled)));
        btnTransport.setOnClickListener(v -> logAction(getString(R.string.public_transport)));
        btnElectricity.setOnClickListener(v -> logAction(getString(R.string.saved_electricity)));

        fetchLogsFromFirebase();
    }

    private void logAction(String action) {
        String logId = dbRef.push().getKey();
        long timestamp = System.currentTimeMillis();

        LogModel log = new LogModel(action, timestamp);
        assert logId != null;
        dbRef.child(logId).setValue(log).addOnSuccessListener(unused -> {
            Toast.makeText(this, getString(R.string.action_logged), Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchLogsFromFirebase() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                logsList.clear();
                recycleCount = transportCount = electricityCount = 0;

                for (DataSnapshot data : snapshot.getChildren()) {
                    LogModel log = data.getValue(LogModel.class);
                    logsList.add(log);

                    // Update counter for chart
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

                logsAdapter.notifyDataSetChanged();
                updateChart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TrackerActivity.this, getString(R.string.failed_to_load_logs), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateChart() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, recycleCount));
        entries.add(new BarEntry(1f, transportCount));
        entries.add(new BarEntry(2f, electricityCount));

        BarDataSet dataSet = new BarDataSet(entries, getString(R.string.eco_actions));
        dataSet.setColors(new int[]{R.color.colorPrimary, R.color.colorAccent, R.color.colorOrange}, this);
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.8f);

        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(new String[]{getString(R.string.recycle), getString(R.string.transport), getString(R.string.electricity)}));
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.animateY(1000);
        barChart.invalidate();
    }
}
