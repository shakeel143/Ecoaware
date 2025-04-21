package com.example.ecoguardians.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecoguardians.R;

import java.util.Locale;
// SettingsActivity allows the user to change the language of the application
public class SettingsActivity extends AppCompatActivity {

    // Constants for SharedPreferences and language keys
    private static final String PREF_LANG = "language_pref"; // SharedPreferences key for storing language preference
    private static final String KEY_SELECTED_LANG = "selected_lang"; // Key for the selected language in SharedPreferences

    private Spinner languageSpinner; // Spinner for selecting the language
    private String currentLang; // Variable to store the current language code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enables edge-to-edge layout
        setContentView(R.layout.activity_settings); // Set the content view to the settings activity layout

        // Initialize the UI elements
        languageSpinner = findViewById(R.id.language_spinner); // Find the Spinner for language selection
        ImageView imgBack = findViewById(R.id.imgBack); // Find the back button (to close the activity)

        // Set up the back button to finish the activity when clicked
        imgBack.setOnClickListener(v -> finish());

        // Step 1: Load the previously saved language preference from SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREF_LANG, MODE_PRIVATE);
        currentLang = prefs.getString(KEY_SELECTED_LANG, "en"); // Default to "en" (English) if no language is saved

        // Step 2: Set up the language spinner with localized names for different languages
        String[] languageDisplayNames = {
                getString(R.string.english),  // Display name for English
                getString(R.string.spanish),  // Display name for Spanish
                getString(R.string.welsh)     // Display name for Welsh
        };

        // Create an ArrayAdapter to populate the spinner with the language names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, languageDisplayNames);
        languageSpinner.setAdapter(adapter); // Set the adapter for the spinner

        // Step 3: Set the current language as the selected item in the spinner based on saved preference
        int index = getLanguageIndex(currentLang); // Get the index of the saved language
        languageSpinner.setSelection(index); // Set the spinner selection to the saved language

        // Step 4: Handle item selection in the spinner (i.e., when the user selects a new language)
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the language code based on the selected spinner item
                String selectedLang = getLanguageCode(position);

                // Only change the locale if a new language is selected
                if (!selectedLang.equals(currentLang)) {
                    currentLang = selectedLang; // Update the current language variable
                    setLocale(selectedLang); // Set the new locale

                    // Save the new language preference to SharedPreferences
                    prefs.edit().putString(KEY_SELECTED_LANG, selectedLang).apply();

                    recreate(); // Reload the activity to apply the new language settings
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // This method is not used, but required by the AdapterView.OnItemSelectedListener interface
            }
        });
    }

    // Helper method to get the index for the language spinner based on the language code
    private int getLanguageIndex(String langCode) {
        switch (langCode) {
            case "es":
                return 1; // Spanish
            case "cy":
                return 2; // Welsh
            default:
                return 0; // Default to English
        }
    }

    // Helper method to get the language code based on the selected spinner position
    private String getLanguageCode(int position) {
        switch (position) {
            case 1:
                return "es"; // Spanish
            case 2:
                return "cy"; // Welsh
            default:
                return "en"; // Default to English
        }
    }

    // Method to set the locale of the app based on the selected language code
    private void setLocale(String lang) {
        Locale locale = new Locale(lang); // Create a Locale object for the selected language
        Locale.setDefault(locale); // Set the default locale for the app

        // Update the configuration with the new locale
        Resources res = getResources();
        Configuration config = res.getConfiguration();
        config.setLocale(locale); // Set the locale in the configuration

        // Apply the new configuration to the app's resources
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    // Static method to retrieve the selected language string (e.g., "English", "Spanish", "Welsh")
    public static String getSelectedLanguageString(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_LANG, Context.MODE_PRIVATE);
        String selectedLangCode = prefs.getString(KEY_SELECTED_LANG, "en"); // Get the saved language code

        // Return the appropriate string based on the saved language code
        switch (selectedLangCode) {
            case "en":
                return context.getString(R.string.english);
            case "es":
                return context.getString(R.string.spanish);
            case "cy":
                return context.getString(R.string.welsh);
            default:
                return context.getString(R.string.english); // Default to English if the code is unknown
        }
    }

}
