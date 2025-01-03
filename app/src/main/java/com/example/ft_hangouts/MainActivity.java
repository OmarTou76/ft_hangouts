package com.example.ft_hangouts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


import com.example.ft_hangouts.Adapters.ContactAdapter;
import com.example.ft_hangouts.models.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Contact> contacts;

    EditText searchContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContactDatabaseHelper store = new ContactDatabaseHelper(this);
        this.addMockContacts(store);
        contacts = (ArrayList<Contact>) store.getAllContacts();
        RecyclerView contactsView = findViewById(R.id.rv_contact);
        ContactAdapter contactAdapter = new ContactAdapter(this, contacts);

        contactsView.setAdapter(contactAdapter);
        contactsView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton addContact = findViewById(R.id.add_contact);
        addContact.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateContactActivity.class);
            this.startActivity(intent);
        });
        searchContact = findViewById(R.id.search_contact);
        searchContact.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
              String[] words = s.toString().split(" ");
              List<Contact> filteredContact = store.searchContacts(words);

              contactAdapter.updateContacts(filteredContact);
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });
    }

    public void addMockContacts(ContactDatabaseHelper store) {
        if (!store.getAllContacts().isEmpty())
            return;
        store.addContact(new Contact("Alice", "Martin", "0123456789", "alice.martin@example.com"));
        store.addContact(new Contact("Bob", "Dupont", "0612345678", "bob.dupont@example.com"));
        store.addContact(new Contact("Claire", "Leroy", "0789123456", "claire.leroy@example.com"));
        store.addContact(new Contact("David", "Moreau", "0654321098", "david.moreau@example.com"));
    }


}