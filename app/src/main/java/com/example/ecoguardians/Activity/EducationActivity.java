package com.example.ecoguardians.Activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoguardians.Adapter.EducationAdapter;
import com.example.ecoguardians.Model.EducationItem;
import com.example.ecoguardians.R;

import java.util.ArrayList;
import java.util.List;

// EducationActivity displays a list of educational items in a RecyclerView
public class EducationActivity extends AppCompatActivity {

    // Declare RecyclerView to display educational content
    RecyclerView recyclerView;

    // List to hold educational data
    List<EducationItem> educationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for this activity
        setContentView(R.layout.activity_education);

        // Initialize RecyclerView from layout
        recyclerView = findViewById(R.id.educationRecycler);

        // Back button setup
        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> finish()); // Finish activity when back button is clicked

        // Initialize the list and populate it with dummy educational data
        educationList = new ArrayList<>();

        // Add a Climate Change item to the list
        educationList.add(new EducationItem(
                getString(R.string.climate_change_title),    // Title
                getString(R.string.climate_change_subtitle), // Subtitle
                getString(R.string.climate_change_desc),     // Description
                R.drawable.climate));                        // Image

        // Add a Pollution item
        educationList.add(new EducationItem(
                getString(R.string.pollution_title),
                getString(R.string.pollution_subtitle),
                getString(R.string.pollution_desc),
                R.drawable.pollution));

        // Add a Biodiversity item
        educationList.add(new EducationItem(
                getString(R.string.biodiversity_title),
                getString(R.string.biodiversity_subtitle),
                getString(R.string.biodiversity_desc),
                R.drawable.biodiversity));

        // Add a Sustainability item
        educationList.add(new EducationItem(
                getString(R.string.sustainable_title),
                getString(R.string.sustainable_subtitle),
                getString(R.string.sustainable_desc),
                R.drawable.sustainable));

        // Set the layout manager for the RecyclerView to a vertical list
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set adapter to bind the data to the RecyclerView
        recyclerView.setAdapter(new EducationAdapter(this, educationList));
    }
}
