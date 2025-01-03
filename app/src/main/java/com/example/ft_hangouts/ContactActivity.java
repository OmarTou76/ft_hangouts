package com.example.ft_hangouts;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ft_hangouts.models.Contact;

public class ContactActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        TextView name = findViewById(R.id.name);
        TextView phone = findViewById(R.id.phone);

        int id = getIntent().getIntExtra("ID", -1);
        if (id == -1) {
        }

        Contact contact;
        try (ContactDatabaseHelper store = new ContactDatabaseHelper(this)) {

            contact = store.getContactById(id);
        }
        if (contact == null)
            return;

        name.setText(String.format("%s %s", contact.getFirstName(), contact.getLastName()));
        phone.setText(contact.getPhone());
    }
}
