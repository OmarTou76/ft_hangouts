package com.example.ft_hangouts.utils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ft_hangouts.R;
import com.example.ft_hangouts.activities.SettingsActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class AToolbar extends AppCompatActivity {
    private static final String HEADER_PREFS = "HeaderPrefs";
    private static final String KEY_HEADER_COLOR = "header_color";


    public static final String LANGUAGE_PREFS = "LanguagePrefs";
    public static final String LANGUAGE_CODE = "language_code";
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateLanguage();
        setContentView(getLayoutResource());
        configureToolbar();
        setupBackPressedCallback();
        applySavedHeaderColor();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLanguage();
        applySavedHeaderColor();

        long backgroundTime = App.getLastBackgroundTime();
        if (backgroundTime != 0) {
            String time = formatTime(backgroundTime);
            Toast.makeText(this, "App went to background at: " + time, Toast.LENGTH_LONG).show();
            App.resetLastBackgroundTime();
        }
    }

    private String formatTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    private void updateLanguage() {
        if (LocaleHelper.getLocale(this).get(0).getLanguage().equalsIgnoreCase(getSavedLanguage()))
            return;
        LocaleHelper.setLocale(this, getSavedLanguage());
        recreate();
    }

    @LayoutRes
    protected abstract int getLayoutResource();

    protected void applySavedHeaderColor() {
       int savedColor = getSavedHeaderColor();
       toolbar.setBackgroundColor(savedColor);
    }

    protected void saveHeaderColor(int color) {
        SharedPreferences prefs = getSharedPreferences(HEADER_PREFS, MODE_PRIVATE);
        prefs.edit().putInt(KEY_HEADER_COLOR, color).apply();
    }

    protected int getSavedHeaderColor() {
        SharedPreferences sharedPref = getSharedPreferences(HEADER_PREFS, MODE_PRIVATE);
        int savedColor = sharedPref.getInt(KEY_HEADER_COLOR, -1);
        if (savedColor == -1) {
            savedColor = getResources().getColor(R.color.primary, null);
            saveHeaderColor(savedColor);
        }
        return savedColor;
    }

    protected void saveLanguage(String languageCode) {
       SharedPreferences prefs = getSharedPreferences(LANGUAGE_PREFS, MODE_PRIVATE);
       prefs.edit().putString(LANGUAGE_CODE, languageCode).apply();
    }
    protected String getSavedLanguage() {
        SharedPreferences prefs = getSharedPreferences(LANGUAGE_PREFS, MODE_PRIVATE);
        return prefs.getString(LANGUAGE_CODE, "en");
    }

    protected abstract String getActivityTitle();

    protected void setToolbarTitle(String title) {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title);
    }

    private void configureToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getActivityTitle());

        boolean shouldDisplayHomeUp = !isTaskRoot() && getSupportFragmentManager().getBackStackEntryCount() == 0;

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(shouldDisplayHomeUp);
        }

        Drawable drawable = toolbar.getNavigationIcon();
        if (drawable != null) {
            drawable.setTint(Color.WHITE);
        }

        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }
    private void setupBackPressedCallback() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isTaskRoot()) {
                    finishAffinity();
                } else {
                    if (!isFinishing()) {
                        finish();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getLayoutResource() != R.layout.activity_settings)
            getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
