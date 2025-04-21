package com.example.ecoguardians.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoguardians.Activity.CommunityActivity;
import com.example.ecoguardians.Activity.EducationActivity;
import com.example.ecoguardians.Activity.ResourcesActivity;
import com.example.ecoguardians.Activity.SettingsActivity;
import com.example.ecoguardians.Activity.TipsActivity;
import com.example.ecoguardians.Activity.TrackerActivity;
import com.example.ecoguardians.Model.DashboardItem;
import com.example.ecoguardians.R;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private Context context;
    private List<DashboardItem> itemList;

    public DashboardAdapter(Context context, List<DashboardItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            icon = itemView.findViewById(R.id.icon);
        }
    }

    @NonNull
    @Override
    public DashboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.dashboard_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardAdapter.ViewHolder holder, int position) {
        DashboardItem item = itemList.get(position);
        holder.title.setText(item.title);
        holder.icon.setImageResource(item.icon);
        holder.itemView.setOnClickListener(view -> {
            String title = item.title;
            if (title != null) {
                if (title.equalsIgnoreCase(context.getString(R.string.dashboard_education))) {
                    context.startActivity(new Intent(context, EducationActivity.class));
                } else if (title.equalsIgnoreCase(context.getString(R.string.dashboard_tips_challenges))) {
                    context.startActivity(new Intent(context, TipsActivity.class));
                } else if (title.equalsIgnoreCase(context.getString(R.string.dashboard_tracker))) {
                    context.startActivity(new Intent(context, TrackerActivity.class));
                } else if (title.equalsIgnoreCase(context.getString(R.string.dashboard_community))) {
                    context.startActivity(new Intent(context, CommunityActivity.class));
                } else if (title.equalsIgnoreCase(context.getString(R.string.dashboard_resources))) {
                    context.startActivity(new Intent(context, ResourcesActivity.class));
                } else if (title.equalsIgnoreCase(context.getString(R.string.dashboard_settings))) {
                    context.startActivity(new Intent(context, SettingsActivity.class));
                }
                // You can add an 'else' block here to handle cases where the title doesn't match any of the above
            }
            // You can also add an 'else' block here to handle the case where item.title is null
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
