package com.example.ecoguardians.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoguardians.Adapter.ResourceAdapter;
import com.example.ecoguardians.Model.ResourceItem;
import com.example.ecoguardians.R;

import java.util.ArrayList;
import java.util.List;

public class ResourceFragment extends Fragment {

    private static final String ARG_CATEGORY = "category";
    private String category;

    public static ResourceFragment newInstance(String category) {
        ResourceFragment fragment = new ResourceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString(ARG_CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resource, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ResourceAdapter adapter = new ResourceAdapter(getResourcesForCategory(category, getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    public List<ResourceItem> getResourcesForCategory(String category, Context context) {
        List<ResourceItem> items = new ArrayList<>();
        String[] titles = new String[0];
        String[] descriptions = new String[0];
        String[] links = new String[0];

        if (category.equals("Recycling")) {
            titles = context.getResources().getStringArray(R.array.recycling_titles);
            descriptions = context.getResources().getStringArray(R.array.recycling_descriptions);
            links = context.getResources().getStringArray(R.array.recycling_links);
        } else if (category.equals("Gardening")) {
            titles = context.getResources().getStringArray(R.array.gardening_titles);
            descriptions = context.getResources().getStringArray(R.array.gardening_descriptions);
            links = context.getResources().getStringArray(R.array.gardening_links);
        } else if (category.equals("Energy")) {
            titles = context.getResources().getStringArray(R.array.energy_titles);
            descriptions = context.getResources().getStringArray(R.array.energy_descriptions);
            links = context.getResources().getStringArray(R.array.energy_links);
        }

        for (int i = 0; i < titles.length; i++) {
            items.add(new ResourceItem(titles[i], descriptions[i], links[i]));
        }

        return items;
    }


}
