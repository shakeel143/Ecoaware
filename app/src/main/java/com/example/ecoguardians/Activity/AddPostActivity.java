package com.example.ecoguardians.Activity;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ecoguardians.Model.PostModel;
import com.example.ecoguardians.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
// Import required classes and libraries
public class AddPostActivity extends AppCompatActivity {

    // Declare UI components
    private EditText editMessage, editImageUrl;
    private ImageView imagePreview;
    private MaterialButton btnPost;
    private ProgressBar progressBar;

    // Firebase references
    private DatabaseReference postsRef;
    private FirebaseAuth mAuth;

    // Progress dialog to show loading indicator
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge layout support (Android 13+)
        EdgeToEdge.enable(this);

        // Set the layout for this activity
        setContentView(R.layout.activity_add_post);

        // Initialize UI components
        editMessage = findViewById(R.id.editMessage); // Message input field
        editImageUrl = findViewById(R.id.editImageUrl); // Image URL input field
        imagePreview = findViewById(R.id.imagePreview); // Image preview
        btnPost = findViewById(R.id.btnPost); // Post button
        progressBar = findViewById(R.id.progressBar); // Progress bar (optional, not used in this logic)

        // Back button image
        ImageView imgBack = findViewById(R.id.imgBack);

        // When back button is clicked, close the activity
        imgBack.setOnClickListener(v -> finish());

        // Initialize Firebase authentication and database reference
        mAuth = FirebaseAuth.getInstance();
        postsRef = FirebaseDatabase.getInstance().getReference("CommunityPosts");

        // Initialize progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // Prevent user from dismissing it manually

        // Set listener to preview image when image URL input loses focus
        editImageUrl.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) { // When focus is lost
                String url = editImageUrl.getText().toString().trim();
                if (!TextUtils.isEmpty(url)) {
                    // Load image from URL into the ImageView using Glide
                    Glide.with(this).load(url).into(imagePreview);
                    imagePreview.setVisibility(View.VISIBLE); // Make image visible
                }
            }
        });

        // Set listener on post button to create post
        btnPost.setOnClickListener(v -> createPost());
    }

    // Method to handle post creation logic
    private void createPost() {
        // Get message and image URL from input fields
        String message = editMessage.getText().toString().trim();
        String imageUrl = editImageUrl.getText().toString().trim();

        // Validate that message is not empty
        if (TextUtils.isEmpty(message)) {
            editMessage.setError(getString(R.string.enter_a_message));
            return;
        }

        // Show loading dialog
        progressDialog.setMessage(getString(R.string.posting));
        progressDialog.show();

        // Generate unique post ID
        String postId = postsRef.push().getKey();

        // Get current user ID
        String userId = mAuth.getCurrentUser().getUid();

        // Get current timestamp
        long timestamp = System.currentTimeMillis();

        // Create PostModel object with data
        PostModel post = new PostModel(postId, userId, message,
                TextUtils.isEmpty(imageUrl) ? null : imageUrl, timestamp);

        // Save post to Firebase Database
        postsRef.child(postId).setValue(post)
                .addOnSuccessListener(unused -> {
                    // On successful post
                    progressDialog.dismiss(); // Hide loading dialog
                    Toast.makeText(this, getString(R.string.post_created), Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity
                })
                .addOnFailureListener(e -> {
                    // On failure to post
                    progressDialog.dismiss(); // Hide loading dialog
                    Toast.makeText(this, getString(R.string.failed_to_post), Toast.LENGTH_SHORT).show();
                });
    }
}
