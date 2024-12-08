package com.example.ft_hangouts;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    final private String[] Values = new String[5];
    private int counter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.textView);
        Button button = findViewById(R.id.updateText);
        SetStringValues();

        button.setOnClickListener(v -> {
            counter--;
            if (counter >= 0) {
                String text = "Form will be open after %s clicks".format(Values[counter]);
                textView.setText(text);
                return;
            }
            counter = 5;
            Intent intent = new Intent(this, FormUser.class);
            startActivity(intent);
        });
    }

    private void SetStringValues() {
        this.Values[0] = "Zero";
        this.Values[1] = "One";
        this.Values[2] = "Two";
        this.Values[3] = "Three";
        this.Values[4] = "Four";
    }
}