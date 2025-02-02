package com.example.ft_hangouts.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ft_hangouts.utils.AToolbar;
import com.example.ft_hangouts.utils.ContactDatabaseHelper;
import com.example.ft_hangouts.R;
import com.example.ft_hangouts.models.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ContactActivity extends AToolbar {

    private Contact contact;

    private void fetchAndDisplayContact() {
        TextView name = findViewById(R.id.name);
        TextView phone = findViewById(R.id.phone_number);
        TextView email = findViewById(R.id.email);
        int id = getIntent().getIntExtra("ID", -1);
        if (id == -1) {
            this.startActivity(new Intent(this, MainActivity.class));
            Toast.makeText(this, R.string.contact_not_found, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        }

        try (ContactDatabaseHelper store = new ContactDatabaseHelper(this)) {
            this.contact = store.getContactById(id);
        }

        if (this.contact == null)
            finish();

        name.setText(String.format("%s %s", this.contact.getFirstName(), this.contact.getLastName()));
        email.setText(String.format("%s", this.contact.getEmail()));
        phone.setText(String.format("%s", this.contact.getPhone()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setUpButtons();
        fetchAndDisplayContact();
        handleButtonDelete();
    }

    @Override
    protected void onResume() {
        super.onResume();

        fetchAndDisplayContact();
        this.setUpButtons();
    }

    public void deleteContact(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_contact_dialog_title)
                .setMessage(R.string.delete_contact_dialog_msg)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    try (ContactDatabaseHelper store = new ContactDatabaseHelper(this)) {
                        store.deleteContact(this.contact);
                    }
                    finish();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void handleButtonDelete() {
        FloatingActionButton deleteContactPortrait = findViewById(R.id.delete_contact_portrait);
        FloatingActionButton deleteContactLandscape = findViewById(R.id.delete_contact_landscape);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            deleteContactPortrait.setVisibility(View.VISIBLE);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            deleteContactLandscape.setVisibility(View.VISIBLE);
        }
        deleteContactPortrait.setOnClickListener(this::deleteContact);
        deleteContactLandscape.setOnClickListener(this::deleteContact);
    }

    @Override
    protected int getLayoutResource() { return R.layout.activity_contact; }

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
