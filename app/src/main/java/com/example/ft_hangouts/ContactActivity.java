package com.example.ft_hangouts;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ft_hangouts.models.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ContactActivity extends AppCompatActivity {

    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        TextView name = findViewById(R.id.name);
        TextView phone = findViewById(R.id.phone_number);
        TextView email = findViewById(R.id.email);
        this.setUpButtons();

        int id = getIntent().getIntExtra("ID", -1);
        if (id == -1) {
            this.startActivity(new Intent(this, MainActivity.class));
            Toast.makeText(this, "Contact not found", Toast.LENGTH_SHORT).show();
            return;
        }

        try (ContactDatabaseHelper store = new ContactDatabaseHelper(this)) {
            this.contact = store.getContactById(id);
        }

        if (this.contact == null)
            return;

        name.setText(String.format("%s %s", this.contact.getFirstName(), this.contact.getLastName()));
        email.setText(String.format("%s", this.contact.getEmail()));
        phone.setText(String.format("%s", this.contact.getPhone()));

        FloatingActionButton deleteContact = findViewById(R.id.delete_contact);
        deleteContact.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete contact")
                    .setMessage("Confirm deletion?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        try (ContactDatabaseHelper store = new ContactDatabaseHelper(this)) {
                            store.deleteContact(this.contact);
                        }
                        Intent intent = new Intent(this, MainActivity.class);
                        this.startActivity(intent);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @FunctionalInterface
    public interface ButtonCallback {
        void onButtonClick(ImageButton button);
    }

    private void setUpButtons() {
        handleButton(R.id.icon_btn_left, android.R.drawable.sym_action_call, R.string.call,  button -> button.setOnClickListener(v -> this.callContact()));
        handleButton(R.id.icon_btn_mid, android.R.drawable.sym_action_email, R.string.send_message, button -> button.setOnClickListener(v -> this.textContact()));
        handleButton(R.id.icon_btn_right, android.R.drawable.ic_menu_edit, R.string.edit, button -> button.setOnClickListener(v -> this.editContact()));
    }

    private void handleButton(int buttonId, int iconId, int labelId, ButtonCallback callback) {
        View container = findViewById(buttonId);
        if (container == null) {
            return;
        }
        ImageButton btn = (ImageButton)container.findViewById(R.id.img_btn);
        TextView label = container.findViewById(R.id.labelText);
        if (btn == null || label == null) {
            return;
        }
        label.setText(labelId);

        btn.setImageResource(iconId);
        callback.onButtonClick(btn);
    }

    private void callContact() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        String url = String.format("tel:%s", this.contact.getPhone());
        intent.setData(Uri.parse(url));
        this.startActivity(intent);
    }
    private void textContact() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        String url = String.format("smsto:%s", this.contact.getPhone());
        intent.setData(Uri.parse(url));
        this.startActivity(intent);
    }

    private void editContact() {
        Intent intent = new Intent(this, CreateContactActivity.class);
        intent.putExtra("ID", contact.getId());
        this.startActivity(intent);
    }
}
