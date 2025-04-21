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

public class EducationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<EducationItem> educationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);

        recyclerView = findViewById(R.id.educationRecycler);
        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> finish());
        // Dummy Data
        educationList = new ArrayList<>();
        educationList.add(new EducationItem(
                getString(R.string.climate_change_title),
                getString(R.string.climate_change_subtitle),
                getString(R.string.climate_change_desc),
                R.drawable.climate));

        educationList.add(new EducationItem(
                getString(R.string.pollution_title),
                getString(R.string.pollution_subtitle),
                getString(R.string.pollution_desc),
                R.drawable.pollution));

        educationList.add(new EducationItem(
                getString(R.string.biodiversity_title),
                getString(R.string.biodiversity_subtitle),
                getString(R.string.biodiversity_desc),
                R.drawable.biodiversity));

        educationList.add(new EducationItem(
                getString(R.string.sustainable_title),
                getString(R.string.sustainable_subtitle),
                getString(R.string.sustainable_desc),
                R.drawable.sustainable));


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new EducationAdapter(this, educationList));
    }
}
