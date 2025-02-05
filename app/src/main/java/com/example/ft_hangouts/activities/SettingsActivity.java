package com.example.ft_hangouts.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.example.ft_hangouts.utils.AToolbar;
import com.example.ft_hangouts.R;
import com.example.ft_hangouts.utils.LocaleHelper;

import java.util.Objects;

public class SettingsActivity extends AToolbar {
    private RadioButton radioBlack, radioOrange, radioBlue, radioFrench, radioEnglish;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       radioBlack = findViewById(R.id.radio_black);
       radioOrange = findViewById(R.id.radio_orange);
       radioBlue = findViewById(R.id.radio_blue);

       radioEnglish = findViewById(R.id.radio_english);
       radioFrench = findViewById(R.id.radio_french);

       handleHeaderColorRadio();
       handleLanguage();
   }

    @Override
    protected int getLayoutResource() { return R.layout.activity_settings; }

    @Override
    protected String getActivityTitle() { return getString(R.string.settings); }

   private void handleHeaderColorRadio() {
       radioBlack.setOnClickListener(this::onHeaderColorClicked);
       radioBlue.setOnClickListener(this::onHeaderColorClicked);
       radioOrange.setOnClickListener(this::onHeaderColorClicked);

       int currentColor = getSavedHeaderColor();
       if (currentColor == getResources().getColor(R.color.primary, null)) {
           radioBlack.setChecked(true);
       } else if (currentColor == getResources().getColor(R.color.secondary, null)) {
           radioOrange.setChecked(true);
       } else if (currentColor == getResources().getColor(R.color.third, null)) {
           radioBlue.setChecked(true);
       }
   }

   private void handleLanguage() {
       radioEnglish.setOnClickListener(this::onLanguageClicked);
       radioFrench.setOnClickListener(this::onLanguageClicked);

       String currentLanguage = getSavedLanguage();
       if (Objects.equals(currentLanguage, "fr"))
           radioFrench.setChecked(true);
       else
           radioEnglish.setChecked(true);
   }
   private void onLanguageClicked(View view) {
       radioEnglish.setChecked(false);
       radioFrench.setChecked(false);
       String languageCode = "en";

       if (view.getId() == R.id.radio_french) {
           languageCode = "fr";
       }

       ((RadioButton)view).setChecked(true);
       saveLanguage(languageCode);
       LocaleHelper.setLocale(this, languageCode);
       recreate();
   }

   private void onHeaderColorClicked(View view) {
       radioBlack.setChecked(false);
       radioBlue.setChecked(false);
       radioOrange.setChecked(false);

       ((RadioButton)view).setChecked(true);
       if (view.getId() == R.id.radio_black) {
           saveHeaderColor(getResources().getColor(R.color.primary, null));
       } else if (view.getId() == R.id.radio_orange) {
           saveHeaderColor(getResources().getColor(R.color.secondary, null));
       } else if (view.getId() == R.id.radio_blue) {
           saveHeaderColor(getResources().getColor(R.color.third, null));
       }
       applySavedHeaderColor();
   }
}
