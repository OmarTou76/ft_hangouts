package com.example.ft_hangouts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;


import com.example.ft_hangouts.models.Contact;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Contact> contacts;

    EditText searchContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contacts = ContactsSingleton.getInstance().getContacts();
        RecyclerView contactsView = findViewById(R.id.rv_contact);
        ContactAdapter contactAdapter = new ContactAdapter(this, contacts);

        contactsView.setAdapter(contactAdapter);
        contactsView.setLayoutManager(new LinearLayoutManager(this));

       searchContact = findViewById(R.id.search_contact);
       searchContact.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
              String[] words = s.toString().split(" ");
               Log.d("start: ", "");
              for (String word : words) {
                  Log.d("Word: ", word);
              }
              List<Contact> ctn = ContactsSingleton.getInstance().filterContact(words);
               for (Contact cont : ctn) {
                   Log.d("Name: ", cont.getFirstName());
               }
              contactAdapter.updateContacts(ctn);
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });
    }


}