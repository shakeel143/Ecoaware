package com.example.ecoguardians.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoguardians.Model.ResourceItem;
import com.example.ecoguardians.R;

import java.util.List;

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.ResourceViewHolder> {

    private List<ResourceItem> resourceList;
    private int expandedPosition = -1;

    public ResourceAdapter(List<ResourceItem> resourceList) {
        this.resourceList = resourceList;
    }

    @NonNull
    @Override
    public ResourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resource, parent, false);
        return new ResourceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResourceViewHolder holder, int position) {
        ResourceItem item = resourceList.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.link.setText(item.getLink());

        final boolean isExpanded = position == expandedPosition;
        holder.description.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.link.setVisibility(isExpanded && item.getLink() != null ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            expandedPosition = isExpanded ? -1 : position;
            notifyDataSetChanged();
        });

        holder.link.setOnClickListener(v -> {
            if (item.getLink() != null && !item.getLink().isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
                holder.itemView.getContext().startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resourceList.size();
    }

    public static class ResourceViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, link;

        public ResourceViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleText);
            description = itemView.findViewById(R.id.descriptionText);
            link = itemView.findViewById(R.id.linkText);
        }
    }
}

