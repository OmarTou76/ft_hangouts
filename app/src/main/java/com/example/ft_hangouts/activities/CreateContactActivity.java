package com.example.ft_hangouts.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ft_hangouts.utils.AToolbar;
import com.example.ft_hangouts.utils.ContactDatabaseHelper;
import com.example.ft_hangouts.R;
import com.example.ft_hangouts.models.Contact;

public class CreateContactActivity extends AToolbar {

    ContactDatabaseHelper store;
    Contact contact = null;

    EditText firstName;
    EditText lastName;
    EditText phoneNumber;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firstName = (EditText)findViewById(R.id.first_name);
        lastName = (EditText)findViewById(R.id.last_name);
        phoneNumber = (EditText)findViewById(R.id.phone_number);
        email = (EditText)findViewById(R.id.email);

        store = new ContactDatabaseHelper(this);
        int id = getIntent().getIntExtra("ID", -1);
        if (id != -1) {
            this.contact = store.getContactById(id);
            if (this.contact == null)
                Toast.makeText(this, "Contact not found", Toast.LENGTH_SHORT).show();
            else {
                populateForm();
                Button submit = findViewById(R.id.submit);
                submit.setText(R.string.button_update_contact);
            }
        }
        this.handleForm();
    }

    @Override
    protected int getLayoutResource() { return R.layout.activity_set_contact; }

    private void populateForm() {
        this.firstName.setText(contact.getFirstName());
        this.lastName.setText(contact.getLastName());
        this.phoneNumber.setText(contact.getPhone());
        this.email.setText(contact.getEmail());
    }
    private void handleForm() {
        View submit = findViewById(R.id.submit);

        submit.setOnClickListener(v -> {
            if (!checkForm(firstName, lastName, phoneNumber, email)) {
                Toast.makeText(this, R.string.missing_field, Toast.LENGTH_SHORT).show();
                return;
            }
            int id = -1;
            if (contact != null) {
                id = contact.getId();
            }
            contact = new Contact(firstName.getText().toString(), lastName.getText().toString(),
                    phoneNumber.getText().toString(), email.getText().toString());
            contact.setId(id);
            this.store.setContact(contact);
            this.finish();
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
