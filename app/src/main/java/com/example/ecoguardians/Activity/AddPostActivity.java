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

public class AddPostActivity extends AppCompatActivity {

    private EditText editMessage, editImageUrl;
    private ImageView imagePreview;
    private MaterialButton btnPost;
    private ProgressBar progressBar;

    private DatabaseReference postsRef;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_post);

        editMessage = findViewById(R.id.editMessage);
        editImageUrl = findViewById(R.id.editImageUrl);
        imagePreview = findViewById(R.id.imagePreview);
        btnPost = findViewById(R.id.btnPost);
        progressBar = findViewById(R.id.progressBar);

        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> finish());
        mAuth = FirebaseAuth.getInstance();
        postsRef = FirebaseDatabase.getInstance().getReference("CommunityPosts");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        editImageUrl.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String url = editImageUrl.getText().toString().trim();
                if (!TextUtils.isEmpty(url)) {
                    Glide.with(this).load(url).into(imagePreview);
                    imagePreview.setVisibility(View.VISIBLE);
                }
            }
        });

        btnPost.setOnClickListener(v -> createPost());
    }

    private void createPost() {
        String message = editMessage.getText().toString().trim();
        String imageUrl = editImageUrl.getText().toString().trim();

        if (TextUtils.isEmpty(message)) {
            editMessage.setError(getString(R.string.enter_a_message));
            return;
        }

        progressDialog.setMessage(getString(R.string.posting));
        progressDialog.show();

        String postId = postsRef.push().getKey();
        String userId = mAuth.getCurrentUser().getUid();
        long timestamp = System.currentTimeMillis();

        PostModel post = new PostModel(postId, userId, message,
                TextUtils.isEmpty(imageUrl) ? null : imageUrl, timestamp);

        postsRef.child(postId).setValue(post)
                .addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, getString(R.string.post_created), Toast.LENGTH_SHORT).show();
                    finish(); // Close activity
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, getString(R.string.failed_to_post), Toast.LENGTH_SHORT).show();
                });
    }
}
