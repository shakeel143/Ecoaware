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

public class CommunityActivity extends AppCompatActivity implements PostViewHolder.CommentListener {

    private RecyclerView postRecyclerView;
    private ProgressBar loadingProgressBar;
    private TextView noPostsTextView;
    private FloatingActionButton fabAddPost;

    private DatabaseReference postsRef;
    private DatabaseReference commentsRef;
    private DatabaseReference usersRef;
    private FirebaseRecyclerAdapter<PostModel, PostViewHolder> adapter;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_community);
        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> finish());
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        postsRef = FirebaseDatabase.getInstance().getReference("CommunityPosts");
        commentsRef = FirebaseDatabase.getInstance().getReference("Comments");
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        postRecyclerView = findViewById(R.id.postRecyclerView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        noPostsTextView = findViewById(R.id.noPostsTextView);
        fabAddPost = findViewById(R.id.fabAddPost);

        postRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<PostModel> options = new FirebaseRecyclerOptions.Builder<PostModel>()
                .setQuery(postsRef.orderByChild("timestamp"), PostModel.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<PostModel, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull PostModel model) {
                holder.bind(model, currentUserId);
                holder.setCommentListener(CommunityActivity.this);
            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
                return new PostViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                loadingProgressBar.setVisibility(View.GONE);
                if (getItemCount() == 0) {
                    noPostsTextView.setVisibility(View.VISIBLE);
                } else {
                    noPostsTextView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(@NonNull DatabaseError error) {
                loadingProgressBar.setVisibility(View.GONE);
                noPostsTextView.setText(getString(R.string.error_loading_posts));
                noPostsTextView.setVisibility(View.VISIBLE);
            }
        };

        postRecyclerView.setAdapter(adapter);

        fabAddPost.setOnClickListener(v -> {
            Intent intent = new Intent(CommunityActivity.this, AddPostActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onCommentPosted(String postId, String commentText) {
        DatabaseReference postCommentsRef = commentsRef.child(postId);
        String commentId = postCommentsRef.push().getKey();

        CommentModel comment = new CommentModel(
                commentId,
                currentUserId,
                commentText,
                System.currentTimeMillis()
        );

        // Add user data to comment
        usersRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("name").getValue(String.class);
                    String profileImage = snapshot.child("profileImage").getValue(String.class);

                    comment.setUserName(username);
                    comment.setUserProfileImage(profileImage);

                    // Add comment to database
                    postCommentsRef.child(commentId).setValue(comment)
                            .addOnSuccessListener(aVoid -> {
                                // Update comments count in the post
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

    @Override
    public void onCommentsLoaded(int position, String postId) {
        PostViewHolder holder = (PostViewHolder) postRecyclerView.findViewHolderForAdapterPosition(position);
        if (holder != null) {
            DatabaseReference postCommentsRef = commentsRef.child(postId);

            FirebaseRecyclerOptions<CommentModel> options = new FirebaseRecyclerOptions.Builder<CommentModel>()
                    .setQuery(postCommentsRef.orderByChild("timestamp"), CommentModel.class)
                    .build();

            CommentAdapter commentAdapter = new CommentAdapter(options);
            holder.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(CommunityActivity.this));
            holder.commentsRecyclerView.setAdapter(commentAdapter);
            commentAdapter.startListening();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadingProgressBar.setVisibility(View.VISIBLE);
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}