package com.example.ft_hangouts.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ft_hangouts.utils.AToolbar;
import com.example.ft_hangouts.database.ContactDatabaseHelper;
import com.example.ft_hangouts.R;
import com.example.ft_hangouts.models.Contact;

public class CreateContactActivity extends AToolbar {

    ContactDatabaseHelper store;
    Contact contact = null;

    EditText firstName;
    EditText lastName;
    EditText phoneNumber;
    EditText email;

    EditText nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firstName = (EditText)findViewById(R.id.first_name);
        lastName = (EditText)findViewById(R.id.last_name);
        phoneNumber = (EditText)findViewById(R.id.phone_number);
        email = (EditText)findViewById(R.id.email);
        nickname = (EditText)findViewById(R.id.nickname);

        store = new ContactDatabaseHelper(this);
        int id = getIntent().getIntExtra("ID", -1);
        if (id != -1) {
            this.contact = store.getContactById(id);
            if (this.contact == null) {
                Toast.makeText(this, "Contact not found", Toast.LENGTH_SHORT).show();
                finish();
            }
            else {
                populateForm();
                Button submit = findViewById(R.id.submit);
                submit.setText(R.string.button_update_contact);
                setToolbarTitle(getString(R.string.title_edit_contact));
            }
        }
        this.handleForm();
    }

    @Override
    protected void onResume() {
        super.onResume();

        EditText v = (EditText)findViewById(R.id.first_name);
        if (!v.getHint().toString().equals(getString(R.string.first_name))) {
            recreate();
        }
    }

    @Override
    protected int getLayoutResource() { return R.layout.activity_set_contact; }

    @Override
    protected String getActivityTitle() { return getString(R.string.button_create_contact); }
    private void populateForm() {
        this.firstName.setText(contact.getFirstName());
        this.lastName.setText(contact.getLastName());
        this.phoneNumber.setText(contact.getPhone());
        this.email.setText(contact.getEmail());
    }
    private void handleForm() {
        View submit = findViewById(R.id.submit);

        submit.setOnClickListener(v -> {
            if (firstName.getText().toString().isEmpty()) {
                Toast.makeText(this, R.string.missing_firstname, Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isValidEmail(email.getText().toString())) {
                Toast.makeText(this, R.string.invalid_email, Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isValidPhoneNumber(this.store.normalizePhoneNumber(phoneNumber.getText().toString()))) {
                Toast.makeText(this, R.string.invalid_phone, Toast.LENGTH_SHORT).show();
                return;
            }
            int id = -1;
            if (contact != null) {
                id = contact.getId();
            }
            contact = new Contact(firstName.getText().toString(), lastName.getText().toString(),
                    phoneNumber.getText().toString(), email.getText().toString(), nickname.getText().toString());
            contact.setId(id);
            this.store.setContact(contact);
            this.finish();
        });
    }

    public boolean isValidEmail(String email) {
        Log.d("TEST", email);
        return email.matches("(?i)^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$");
    }

    private static boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.matches("^(\\+33|0)[1-9](\\d{2}){4}$");
    }


}
