package com.example.ecoguardians.Activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecoguardians.R;

import android.content.Intent;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final int SPLASH_DELAY = 3000; // 2 seconds
    private ImageView splashIcon;
    private TextView splashTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        splashIcon = findViewById(R.id.splashIcon);
        splashTitle = findViewById(R.id.splashTitle);

        // Hide the title initially for the fade-in effect
        splashTitle.setAlpha(0f);

        // Create animations
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(splashIcon, "scaleX", 0.5f, 1f);
        scaleXAnimator.setDuration(800);
        scaleXAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(splashIcon, "scaleY", 0.5f, 1f);
        scaleYAnimator.setDuration(800);
        scaleYAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator fadeInTitle = ObjectAnimator.ofFloat(splashTitle, "alpha", 0f, 1f);
        fadeInTitle.setDuration(1200);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator, fadeInTitle);
        animatorSet.start();


        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(() -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                // User is logged in, go to MainActivity
                startActivity(new Intent(Splash.this, MainActivity.class));
            } else {
                // User is not logged in, go to LoginActivity
                startActivity(new Intent(Splash.this, LoginActivity.class));
            }
            finish(); // Close the splash activity so the user can't go back to it
        }, SPLASH_DELAY);
    }
}