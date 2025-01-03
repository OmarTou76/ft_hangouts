package com.example.ft_hangouts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ft_hangouts.models.Contact;

public class CreateContactActivity extends AppCompatActivity {

    ContactDatabaseHelper store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_contact);
        store = new ContactDatabaseHelper(this);
        this.handleForm();
    }

    private void handleForm() {
        View submit = findViewById(R.id.submit);

        submit.setOnClickListener(v -> {
            EditText firstName = (EditText)findViewById(R.id.first_name);
            EditText lastName = (EditText)findViewById(R.id.last_name);
            EditText phoneNumber = (EditText)findViewById(R.id.phone_number);
            EditText email = (EditText)findViewById(R.id.email);
            if (!checkForm(firstName, lastName, phoneNumber, email)) {
                Toast.makeText(this, R.string.missing_field, Toast.LENGTH_SHORT).show();
                return;
            }
            Contact contact = new Contact(firstName.getText().toString(), lastName.getText().toString(),
                    phoneNumber.getText().toString(), email.getText().toString());
            this.store.addContact(contact);
            Toast.makeText(this, R.string.contact_created, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        });
    }

    private boolean checkForm(EditText ...args) {
        for (EditText arg : args) {
            if (arg.getText().toString().isEmpty())
                return false;
        }
        return true;
    }

}
