package com.example.ft_hangouts.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ft_hangouts.ContactActivity;
import com.example.ft_hangouts.R;
import com.example.ft_hangouts.models.Contact;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    Context context;
    List<Contact> contacts;

    public ContactAdapter (Context context, List<Contact> contacts) {
       this.context = context;
       this.contacts = contacts;
    }
    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        private final TextView contactName;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contact_name);
        }

    }

    @NonNull
    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.contact_view, parent, false);
       return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ContactViewHolder holder, int position) {
        Contact contact = this.contacts.get(position);
        holder.contactName.setText(String.format("%s %s", contact.getFirstName(), contact.getLastName()));
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), String.format("%s contact triggered with ID %s !", contact.getFirstName(), contact.getId()), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, ContactActivity.class);
            intent.putExtra("ID", contact.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return this.contacts.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateContacts(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }
}
