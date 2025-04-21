package com.example.ecoguardians.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecoguardians.Model.PostModel;
import com.example.ecoguardians.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class PostViewHolder extends RecyclerView.ViewHolder {
    public TextView tvUsername, tvTimestamp, tvMessage, tvLikes, tvCommentsCount;
    public ImageView imgProfile, imgPost, btnLike, btnComment;
    public LinearLayout commentsSection;
    public RecyclerView commentsRecyclerView;
    public EditText etCommentInput;
    public Button btnPostComment;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        tvUsername = itemView.findViewById(R.id.tvUsername);
        tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        tvMessage = itemView.findViewById(R.id.tvMessage);
        imgPost = itemView.findViewById(R.id.imgPost);
        btnLike = itemView.findViewById(R.id.btnLike);
        btnComment = itemView.findViewById(R.id.btnComment);
        tvLikes = itemView.findViewById(R.id.tvLikes);
        imgProfile = itemView.findViewById(R.id.imgProfile);
        tvCommentsCount = itemView.findViewById(R.id.tvCommentsCount);

        commentsSection = itemView.findViewById(R.id.commentsSection);
        commentsRecyclerView = itemView.findViewById(R.id.commentsRecyclerView);
        etCommentInput = itemView.findViewById(R.id.etCommentInput);
        btnPostComment = itemView.findViewById(R.id.btnPostComment);
    }

    public void bind(PostModel post, String currentUserId) {
        tvMessage.setText(post.getMessage());
        tvLikes.setText(post.getLikesCount() + tvLikes.getContext().getString(R.string.likes));
        tvCommentsCount.setText(post.getCommentsCount() + tvCommentsCount.getContext().getString(R.string.comments));

        // Load post image if available
        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            imgPost.setVisibility(View.VISIBLE);
            Glide.with(itemView.getContext())
                    .load(post.getImageUrl())
                    .into(imgPost);
        } else {
            imgPost.setVisibility(View.GONE);
        }

        // Load user data (username and profile image)
        loadUserData(post.getUserId());

        // Format timestamp
        tvTimestamp.setText(formatTimestamp(post.getTimestamp(), tvTimestamp.getContext()));

        // Handle like button
        btnLike.setOnClickListener(v -> toggleLike(post, currentUserId));

        // Handle comment button click
        btnComment.setOnClickListener(v -> toggleCommentsSection(post));

        // Handle post comment button
        btnPostComment.setOnClickListener(v -> {
            String commentText = etCommentInput.getText().toString().trim();
            if (!commentText.isEmpty()) {
                if (mCommentListener != null) {
                    mCommentListener.onCommentPosted(post.getPostId(), commentText);
                    etCommentInput.setText("");
                }
            } else {
                Toast.makeText(itemView.getContext(), itemView.getContext().getString(R.string.comment_cannot_be_empty), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleCommentsSection(PostModel post) {
        if (commentsSection.getVisibility() == View.VISIBLE) {
            commentsSection.setVisibility(View.GONE);
        } else {
            commentsSection.setVisibility(View.VISIBLE);
            if (mCommentListener != null) {
                mCommentListener.onCommentsLoaded(getAdapterPosition(), post.getPostId());
            }
        }
    }

    private void toggleLike(PostModel post, String currentUserId) {
        DatabaseReference likesRef = FirebaseDatabase.getInstance()
                .getReference("CommunityPosts")
                .child(post.getPostId())
                .child("likes");

        likesRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DatabaseReference postRef = FirebaseDatabase.getInstance()
                        .getReference("CommunityPosts")
                        .child(post.getPostId());

                if (snapshot.exists()) {
                    // Unlike
                    likesRef.child(currentUserId).removeValue();
                    postRef.child("likesCount").setValue(post.getLikesCount() - 1);
                } else {
                    // Like
                    likesRef.child(currentUserId).setValue(true);
                    postRef.child("likesCount").setValue(post.getLikesCount() + 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(itemView.getContext(), itemView.getContext().getString(R.string.failed_to_like_post), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserData(String userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("name").getValue(String.class);
                    String profileImage = snapshot.child("profileImage").getValue(String.class);

                    tvUsername.setText(username);
                    if (profileImage != null && !profileImage.isEmpty()) {
                        Glide.with(itemView.getContext())
                                .load(profileImage)
                                .placeholder(R.drawable.ic_leaf)
                                .into(imgProfile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
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

    // Interface for comment events
    public interface CommentListener {
        void onCommentPosted(String postId, String commentText);

        void onCommentsLoaded(int position, String postId);
    }

    private CommentListener mCommentListener;

    public void setCommentListener(CommentListener listener) {
        mCommentListener = listener;
    }
}