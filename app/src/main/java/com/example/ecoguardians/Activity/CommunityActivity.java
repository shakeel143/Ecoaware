package com.example.ecoguardians.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoguardians.Adapter.CommentAdapter;
import com.example.ecoguardians.Adapter.PostViewHolder;
import com.example.ecoguardians.Model.CommentModel;
import com.example.ecoguardians.Model.PostModel;
import com.example.ecoguardians.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

// CommunityActivity displays posts and allows users to comment on them.
public class CommunityActivity extends AppCompatActivity implements PostViewHolder.CommentListener {

    // UI Components
    private RecyclerView postRecyclerView;
    private ProgressBar loadingProgressBar;
    private TextView noPostsTextView;
    private FloatingActionButton fabAddPost;

    // Firebase Database references
    private DatabaseReference postsRef;
    private DatabaseReference commentsRef;
    private DatabaseReference usersRef;

    // Firebase adapter to display posts in RecyclerView
    private FirebaseRecyclerAdapter<PostModel, PostViewHolder> adapter;

    // Stores current user's UID
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enables edge-to-edge display on supported devices
        EdgeToEdge.enable(this);

        // Set layout for the activity
        setContentView(R.layout.activity_community);

        // Initialize back button
        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> finish()); // Close activity on back press

        // Get current user ID
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Initialize Firebase references
        postsRef = FirebaseDatabase.getInstance().getReference("CommunityPosts");
        commentsRef = FirebaseDatabase.getInstance().getReference("Comments");
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize UI components
        postRecyclerView = findViewById(R.id.postRecyclerView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        noPostsTextView = findViewById(R.id.noPostsTextView);
        fabAddPost = findViewById(R.id.fabAddPost);

        // Set layout manager for RecyclerView
        postRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Firebase options to fetch PostModel data sorted by timestamp
        FirebaseRecyclerOptions<PostModel> options = new FirebaseRecyclerOptions.Builder<PostModel>()
                .setQuery(postsRef.orderByChild("timestamp"), PostModel.class)
                .build();

        // Initialize FirebaseRecyclerAdapter to bind data to view holder
        adapter = new FirebaseRecyclerAdapter<PostModel, PostViewHolder>(options) {

            // Bind each post model to its view holder
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull PostModel model) {
                holder.bind(model, currentUserId); // Bind post data
                holder.setCommentListener(CommunityActivity.this); // Set listener for comment actions
            }

            // Inflate layout for each post item
            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
                return new PostViewHolder(view);
            }

            // Handle data changes (e.g. when post list is updated)
            @Override
            public void onDataChanged() {
                loadingProgressBar.setVisibility(View.GONE); // Hide progress bar
                if (getItemCount() == 0) {
                    noPostsTextView.setVisibility(View.VISIBLE); // Show "no posts" text
                } else {
                    noPostsTextView.setVisibility(View.GONE); // Hide it if posts exist
                }
            }

            // Handle Firebase errors
            @Override
            public void onError(@NonNull DatabaseError error) {
                loadingProgressBar.setVisibility(View.GONE);
                noPostsTextView.setText(getString(R.string.error_loading_posts)); // Display error message
                noPostsTextView.setVisibility(View.VISIBLE);
            }
        };

        // Set adapter to RecyclerView
        postRecyclerView.setAdapter(adapter);

        // Add Post FAB click listener
        fabAddPost.setOnClickListener(v -> {
            // Start AddPostActivity when FAB is clicked
            Intent intent = new Intent(CommunityActivity.this, AddPostActivity.class);
            startActivity(intent);
        });
    }

    // Callback when a comment is posted
    @Override
    public void onCommentPosted(String postId, String commentText) {
        // Reference to comments under a specific post
        DatabaseReference postCommentsRef = commentsRef.child(postId);
        String commentId = postCommentsRef.push().getKey(); // Unique ID for comment

        // Create CommentModel with initial data
        CommentModel comment = new CommentModel(
                commentId,
                currentUserId,
                commentText,
                System.currentTimeMillis()
        );

        // Fetch user data (name and profile image) to attach to comment
        usersRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("name").getValue(String.class);
                    String profileImage = snapshot.child("profileImage").getValue(String.class);

                    // Set additional user data to comment
                    comment.setUserName(username);
                    comment.setUserProfileImage(profileImage);

                    // Save comment under post
                    postCommentsRef.child(commentId).setValue(comment)
                            .addOnSuccessListener(aVoid -> {
                                // Increment comment count in post
                                postsRef.child(postId).child("commentsCount")
                                        .setValue(ServerValue.increment(1));
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(CommunityActivity.this, getString(R.string.failed_to_post_comment), Toast.LENGTH_SHORT).show();
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CommunityActivity.this, getString(R.string.failed_to_load_user_data), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Callback when comments for a post need to be loaded
    @Override
    public void onCommentsLoaded(int position, String postId) {
        // Get the view holder at the given position
        PostViewHolder holder = (PostViewHolder) postRecyclerView.findViewHolderForAdapterPosition(position);
        if (holder != null) {
            // Get reference to the comments of the specific post
            DatabaseReference postCommentsRef = commentsRef.child(postId);

            // Setup FirebaseRecyclerOptions for CommentModel
            FirebaseRecyclerOptions<CommentModel> options = new FirebaseRecyclerOptions.Builder<CommentModel>()
                    .setQuery(postCommentsRef.orderByChild("timestamp"), CommentModel.class)
                    .build();

            // Set up CommentAdapter to load and show comments
            CommentAdapter commentAdapter = new CommentAdapter(options);
            holder.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(CommunityActivity.this));
            holder.commentsRecyclerView.setAdapter(commentAdapter);
            commentAdapter.startListening(); // Start listening for data
        }
    }

    // Start listening for Firebase data when activity starts
    @Override
    protected void onStart() {
        super.onStart();
        loadingProgressBar.setVisibility(View.VISIBLE);
        adapter.startListening(); // Start listening to Firebase data
    }

    // Stop listening when activity stops to avoid memory leaks
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening(); // Stop listening to Firebase data
    }
}
