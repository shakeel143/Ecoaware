package com.example.ecoguardians.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoguardians.Model.EducationItem;
import com.example.ecoguardians.R;

import java.util.List;

public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.ViewHolder> {

    Context context;
    List<EducationItem> list;

    public EducationAdapter(Context context, List<EducationItem> list) {
        this.context = context;
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, shortText, fullText;
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleText);
            shortText = itemView.findViewById(R.id.shortText);
            fullText = itemView.findViewById(R.id.fullText);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    @NonNull
    @Override
    public EducationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.education_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EducationAdapter.ViewHolder holder, int position) {
        EducationItem item = list.get(position);

        holder.title.setText(item.title);
        holder.shortText.setText(item.shortText);
        holder.fullText.setText(item.fullText);
        holder.imageView.setImageResource(item.imageRes);

        // Toggle expand/collapse
        holder.itemView.setOnClickListener(v -> {
            item.isExpanded = !item.isExpanded;
            notifyItemChanged(position);
        });

        holder.fullText.setVisibility(item.isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
