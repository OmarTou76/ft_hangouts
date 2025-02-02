package com.example.ft_hangouts.activities;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RadioButton;

import com.example.ft_hangouts.utils.AToolbar;
import com.example.ft_hangouts.R;

public class SettingsActivity extends AToolbar {

    private RadioButton radioBlack, radioOrange, radioBlue;
    private int currentColor;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       radioBlack = findViewById(R.id.radio_black);
       radioOrange = findViewById(R.id.radio_orange);
       radioBlue = findViewById(R.id.radio_blue);

       handleHeaderColorRadio();
   }

   private void handleHeaderColorRadio() {
       radioBlack.setOnClickListener(this::onRadioButtonClicked);
       radioBlue.setOnClickListener(this::onRadioButtonClicked);
       radioOrange.setOnClickListener(this::onRadioButtonClicked);

       if (getSavedHeaderColor() == getResources().getColor(R.color.primary, null)) {
           radioBlack.setChecked(true);
       } else if (getSavedHeaderColor() == getResources().getColor(R.color.secondary, null)) {
           radioOrange.setChecked(true);
       } else if (getSavedHeaderColor() == getResources().getColor(R.color.third, null)) {
           radioBlue.setChecked(true);
       }
   }
   @Override
    protected int getLayoutResource() { return R.layout.activity_settings; }


    private void onRadioButtonClicked(View view) {
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
