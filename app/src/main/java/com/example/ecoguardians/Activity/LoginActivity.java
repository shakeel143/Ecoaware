package com.example.ecoguardians.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecoguardians.R;
import com.google.firebase.auth.FirebaseAuth;

// LoginActivity handles user authentication using email and password via Firebase
public class LoginActivity extends AppCompatActivity {

    // Declare UI components
    private EditText emailInput, passwordInput;
    private Button loginBtn;
    private TextView signupText;

    // Firebase authentication instance
    private FirebaseAuth mAuth;

    // Dialog shown while logging in
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for the login screen
        setContentView(R.layout.activity_login);

        // Initialize Firebase authentication instance
        mAuth = FirebaseAuth.getInstance();

        // Get references to UI elements from the layout
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.loginBtn);
        signupText = findViewById(R.id.signupText);

        // Set up progress dialog for login process
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.logging_in)); // Title of dialog
        progressDialog.setMessage(getString(R.string.please_wait)); // Message in dialog
        progressDialog.setCancelable(false); // Prevent user from canceling dialog manually

        // Set onClickListener for login button
        loginBtn.setOnClickListener(v -> loginUser());

        // Redirect to signup screen when user clicks the signup text
        signupText.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });
    }

    // Method to handle user login
    private void loginUser() {
        // Get input from user
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        // Validate that fields are not empty
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading dialog
        progressDialog.show();

        // Sign in using Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    // Hide progress dialog after attempt
                    progressDialog.dismiss();

                    // Check if login was successful
                    if (task.isSuccessful()) {
                        Toast.makeText(this, getString(R.string.login_successful), Toast.LENGTH_SHORT).show();

                        // Redirect user to main activity
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish(); // Close login activity
                    } else {
                        // Show error message if login fails
                        Toast.makeText(this, getString(R.string.login_failed) + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
