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

public class SettingsActivity extends AppCompatActivity {

    private static final String PREF_LANG = "language_pref";
    private static final String KEY_SELECTED_LANG = "selected_lang";

    private Spinner languageSpinner;
    private String currentLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        languageSpinner = findViewById(R.id.language_spinner);
        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> finish());
        // Step 1: Load saved language
        SharedPreferences prefs = getSharedPreferences(PREF_LANG, MODE_PRIVATE);
        currentLang = prefs.getString(KEY_SELECTED_LANG, "en");

        // Step 2: Setup spinner with localized names
        String[] languageDisplayNames = {
                getString(R.string.english),
                getString(R.string.spanish),
                getString(R.string.welsh)
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, languageDisplayNames);
        languageSpinner.setAdapter(adapter);

        // Step 3: Set selection based on saved language
        int index = getLanguageIndex(currentLang);
        languageSpinner.setSelection(index);

        // Step 4: Handle selection changes
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLang = getLanguageCode(position);

                // Only change locale if new language is selected
                if (!selectedLang.equals(currentLang)) {
                    currentLang = selectedLang;
                    setLocale(selectedLang);

                    prefs.edit().putString(KEY_SELECTED_LANG, selectedLang).apply();

                    recreate(); // Reload UI
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private int getLanguageIndex(String langCode) {
        switch (langCode) {
            case "es":
                return 1;
            case "cy":
                return 2;
            default:
                return 0; // en
        }
    }

    private String getLanguageCode(int position) {
        switch (position) {
            case 1:
                return "es";
            case 2:
                return "cy";
            default:
                return "en";
        }
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Resources res = getResources();
        Configuration config = res.getConfiguration();
        config.setLocale(locale);

        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    public static String getSelectedLanguageString(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_LANG, Context.MODE_PRIVATE);
        String selectedLangCode = prefs.getString(KEY_SELECTED_LANG, "en");

        switch (selectedLangCode) {
            case "en":
                return context.getString(R.string.english);
            case "es":
                return context.getString(R.string.spanish);
            case "cy":
                return context.getString(R.string.welsh);
            default:
                return context.getString(R.string.english); // Default to English if code is unknown
        }
    }

}