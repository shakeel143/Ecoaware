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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

// SignupActivity handles user registration (sign up) by allowing users to create an account
public class SignupActivity extends AppCompatActivity {

    // Declare UI components
    private EditText nameInput, emailInput, passwordInput; // Fields for user input (name, email, password)
    private Button signupBtn; // Button to trigger user creation
    private TextView loginRedirect; // TextView to redirect to login screen
    private FirebaseAuth mAuth; // Firebase authentication instance for user creation
    private DatabaseReference dbRef; // Firebase database reference to store user data
    private ProgressDialog progressDialog; // ProgressDialog to show loading state during signup

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup); // Set the layout for the activity

        // Initialize FirebaseAuth and DatabaseReference
        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("Users"); // Reference to the "Users" node in Firebase database

        // Initialize the input fields and buttons
        nameInput = findViewById(R.id.nameInput); // Name input field
        emailInput = findViewById(R.id.emailInput); // Email input field
        passwordInput = findViewById(R.id.passwordInput); // Password input field
        signupBtn = findViewById(R.id.signupBtn); // Signup button
        loginRedirect = findViewById(R.id.loginRedirect); // TextView to redirect to login page

        // Initialize the progress dialog (used during signup)
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Signing up"); // Title for the progress dialog
        progressDialog.setMessage("Please wait..."); // Message displayed in the progress dialog
        progressDialog.setCancelable(false); // Prevent the dialog from being dismissed by user interaction

        // Set up the button to trigger user creation process
        signupBtn.setOnClickListener(v -> createUser());

        // Set up the redirect text to take the user to the login screen
        loginRedirect.setOnClickListener(v ->
                startActivity(new Intent(SignupActivity.this, LoginActivity.class))); // Navigate to LoginActivity
    }

    // Method to create a new user in Firebase Authentication and save their data to the Firebase Realtime Database
    private void createUser() {
        // Get user input from the EditText fields
        String name = nameInput.getText().toString().trim(); // Name input
        String email = emailInput.getText().toString().trim(); // Email input
        String password = passwordInput.getText().toString().trim(); // Password input

        // Check if any of the fields are empty
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            // Display a toast message if fields are empty
            Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
            return; // Exit the method if fields are not filled
        }

        // Show the progress dialog while the user is being created
        progressDialog.show();

        // Attempt to create the user using Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // If user creation is successful, get the user's UID
                        String uid = mAuth.getCurrentUser().getUid();

                        // Create a map of user data to store in the database
                        HashMap<String, String> userData = new HashMap<>();
                        userData.put("name", name); // Add name to the map
                        userData.put("email", email); // Add email to the map
                        userData.put("uid", uid); // Add UID to the map
                        userData.put("language", "English"); // Default language is set to English (this can later be changed)

                        // Save the user data to the Firebase Realtime Database
                        dbRef.child(uid).setValue(userData).addOnCompleteListener(dataTask -> {
                            progressDialog.dismiss(); // Dismiss the progress dialog once the database task is complete
                            if (dataTask.isSuccessful()) {
                                // If data is saved successfully, show a success message
                                Toast.makeText(this, getString(R.string.signup_successful), Toast.LENGTH_SHORT).show();
                                // Redirect the user to the login screen
                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                finish(); // Finish the SignupActivity to prevent going back to it
                            } else {
                                // If there was a database error, show an error message
                                Toast.makeText(this, getString(R.string.database_error) + dataTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        progressDialog.dismiss(); // Dismiss the progress dialog if user creation fails
                        // Show an error message if user creation fails
                        Toast.makeText(this, getString(R.string.signup_failed) + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
