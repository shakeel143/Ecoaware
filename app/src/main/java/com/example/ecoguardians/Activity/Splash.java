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
// Splash Activity: This is the initial screen shown when the app starts, which displays a logo and title before navigating to the next activity
public class Splash extends AppCompatActivity {

    // Declare Firebase Authentication instance and UI components
    private FirebaseAuth mAuth; // Firebase Authentication to check if the user is logged in
    private static final int SPLASH_DELAY = 3000; // Splash screen delay time (3 seconds)
    private ImageView splashIcon; // ImageView for the splash screen icon (logo)
    private TextView splashTitle; // TextView for the splash screen title

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge display (full-screen mode)
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash); // Set the layout for the splash activity

        // Handle window insets (system bars like status bar and navigation bar) for edge-to-edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()); // Get the system bar insets
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom); // Set padding for edge-to-edge effect
            return insets;
        });

        // Initialize the UI components
        splashIcon = findViewById(R.id.splashIcon); // Splash icon (logo)
        splashTitle = findViewById(R.id.splashTitle); // Splash title

        // Set the initial alpha of splashTitle to 0 (invisible), will animate to fade in later
        splashTitle.setAlpha(0f);

        // Create animations for splash screen elements
        // Animate the splash icon to grow in size (scaleX and scaleY)
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(splashIcon, "scaleX", 0.5f, 1f);
        scaleXAnimator.setDuration(800); // Duration of the animation (800 milliseconds)
        scaleXAnimator.setInterpolator(new AccelerateDecelerateInterpolator()); // Smooth transition

        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(splashIcon, "scaleY", 0.5f, 1f);
        scaleYAnimator.setDuration(800); // Duration of the animation
        scaleYAnimator.setInterpolator(new AccelerateDecelerateInterpolator()); // Smooth transition

        // Fade in the splash title with an alpha animation
        ObjectAnimator fadeInTitle = ObjectAnimator.ofFloat(splashTitle, "alpha", 0f, 1f);
        fadeInTitle.setDuration(1200); // Duration of the fade-in animation (1200 milliseconds)

        // Combine the animations using an AnimatorSet to play them together
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator, fadeInTitle); // Play the icon scale and title fade-in together
        animatorSet.start(); // Start the animations

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // After the splash delay, check if the user is logged in or not
        new Handler().postDelayed(() -> {
            // Get the current user from Firebase
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                // If user is logged in, go to MainActivity
                startActivity(new Intent(Splash.this, MainActivity.class));
            } else {
                // If user is not logged in, go to LoginActivity
                startActivity(new Intent(Splash.this, LoginActivity.class));
            }
            finish(); // Close the splash activity so the user can't go back to it
        }, SPLASH_DELAY); // Wait for SPLASH_DELAY before navigating to the next activity
    }
}
