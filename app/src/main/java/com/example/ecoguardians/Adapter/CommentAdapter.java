package com.example.ecoguardians.Adapter;


import static com.example.ecoguardians.R.string.*;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoguardians.Model.CommentModel;
import com.example.ecoguardians.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.concurrent.TimeUnit;

public class CommentAdapter extends FirebaseRecyclerAdapter<CommentModel, CommentAdapter.CommentViewHolder> {

    public CommentAdapter(@NonNull FirebaseRecyclerOptions<CommentModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull CommentModel model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentText, commentAuthor, commentTime;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.commentText);
            commentAuthor = itemView.findViewById(R.id.commentAuthor);
            commentTime = itemView.findViewById(R.id.commentTime);
        }

        public void bind(CommentModel comment) {
            commentText.setText(comment.getText());
            // You'll need to fetch user name from Users node using comment.getUserId()
            commentAuthor.setText("User"); // Replace with actual user name
            commentTime.setText(formatTimestamp(comment.getTimestamp(),commentTime.getContext()));
        }

        private String formatTimestamp(long timestamp, Context context) {
            long now = System.currentTimeMillis();
            long difference = now - timestamp;

            long seconds = TimeUnit.MILLISECONDS.toSeconds(difference);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(difference);
            long hours = TimeUnit.MILLISECONDS.toHours(difference);
            long days = TimeUnit.MILLISECONDS.toDays(difference);

            if (seconds < 60) {
                return seconds <= 0 ? context.getString(R.string.just_now) : seconds + context.getString(R.string.seconds_ago);
            } else if (minutes < 60) {
                return minutes + context.getString(R.string.minutes_ago);
            } else if (hours < 24) {
                return hours + context.getString(R.string.hours_ago);
            } else if (days < 2) {
                return context.getString(R.string.yesterday);
            } else {
                // For older posts, you might want to show the date
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM d");
                return sdf.format(new java.util.Date(timestamp));
            }
        }
    }
}