package com.example.ft_hangouts.utils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ft_hangouts.R;
import com.example.ft_hangouts.activities.SettingsActivity;

public abstract class AToolbar extends AppCompatActivity {
    private static final String HEADER_PREFS = "HeaderPrefs";
    private static final String KEY_HEADER_COLOR = "header_color";
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        configureToolbar();
        setupBackPressedCallback();
        applySavedHeaderColor();
    }

    @Override
    protected void onResume() {
        super.onResume();
        applySavedHeaderColor();
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

    private void configureToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
