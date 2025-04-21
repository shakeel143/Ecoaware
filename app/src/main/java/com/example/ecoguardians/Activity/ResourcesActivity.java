package com.example.ecoguardians.Activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ecoguardians.Adapter.ResourcePagerAdapter;
import com.example.ecoguardians.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

// ResourcesActivity displays different tabs related to various resources (e.g., Recycling, Gardening, Energy)
public class ResourcesActivity extends AppCompatActivity {

    // UI elements
    private TabLayout tabLayout; // TabLayout to display tabs at the top
    private ViewPager2 viewPager; // ViewPager2 to swipe through different fragments
    private ResourcePagerAdapter pagerAdapter; // Adapter to manage fragments in the ViewPager2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout for this activity
        setContentView(R.layout.activity_resources);

        // Initialize the back button and set its click listener to finish the activity (go back)
        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> finish());

        // Initialize TabLayout and ViewPager2
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // Create the adapter to handle the fragments and set it to the ViewPager2
        pagerAdapter = new ResourcePagerAdapter(this, ResourcesActivity.this);
        viewPager.setAdapter(pagerAdapter);

        // Use TabLayoutMediator to link the TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    // Set the text for each tab based on the position (each tab corresponds to a resource)
                    switch (position) {
                        case 0:
                            tab.setText(getString(R.string.recycling)); // First tab: Recycling
                            break;
                        case 1:
                            tab.setText(getString(R.string.gardening)); // Second tab: Gardening
                            break;
                        case 2:
                            tab.setText(getString(R.string.energy)); // Third tab: Energy
                            break;
                    }
                }).attach(); // Attach the TabLayout and ViewPager2 together
    }
}
