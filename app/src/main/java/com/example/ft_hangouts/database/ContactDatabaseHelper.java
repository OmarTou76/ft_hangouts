package com.example.ft_hangouts.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ft_hangouts.models.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_CONTACTS = "contacts";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_NICKNAME = "nickname";
    private static final String COLUMN_PHONE_NUMBER = "phone_number";
    private static final String COLUMN_EMAIL = "email";

    public ContactDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_CONTACTS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FIRST_NAME + " TEXT, "
                + COLUMN_LAST_NAME + " TEXT, "
                + COLUMN_NICKNAME + " TEXT, "
                + COLUMN_PHONE_NUMBER + " TEXT, "
                + COLUMN_EMAIL + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_FIRST_NAME, contact.getFirstName());
        values.put(COLUMN_LAST_NAME, contact.getLastName());
        values.put(COLUMN_PHONE_NUMBER, normalizePhoneNumber(contact.getPhone()));
        values.put(COLUMN_EMAIL, contact.getEmail());
        values.put(COLUMN_NICKNAME, contact.getNickname());
        db.insert(TABLE_CONTACTS,null, values);
        db.close();
    }

    public List<Contact> searchContacts(String[] keywords) {
        List<Contact> filteredContacts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM contacts WHERE ");
        List<String> args = new ArrayList<>();

        for (int i = 0; i < keywords.length; i++) {
            if (i > 0) {
                queryBuilder.append(" OR ");
            }
            queryBuilder.append("(first_name LIKE ? OR last_name LIKE ?)");
            args.add("%" + keywords[i] + "%");
            args.add("%" + keywords[i] + "%");
        }

        Cursor cursor = db.rawQuery(queryBuilder.toString(), args.toArray(new String[0]));

        while (cursor.moveToNext()) {
            Contact contact = new Contact();
            contact.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            contact.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)));
            contact.setLastName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)));
            contact.setNickname(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NICKNAME)));
            contact.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER)));
            contact.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
            filteredContacts.add(contact);
        }
        cursor.close();
        db.close();

        return filteredContacts;
    }

    public void setContact(Contact contact) {
        if (contact.getId() == -1) {
            this.addContact(contact);
            return;
        }
        this.updateContact(contact);
    }

    private void updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_FIRST_NAME, contact.getFirstName());
        values.put(COLUMN_LAST_NAME, contact.getLastName());
        values.put(COLUMN_NICKNAME, contact.getNickname());
        values.put(COLUMN_PHONE_NUMBER, normalizePhoneNumber(contact.getPhone()));
        values.put(COLUMN_EMAIL, contact.getEmail());

        String whereClause = COLUMN_ID + " = ?";
        String[] args = new String[]{String.valueOf(contact.getId())};
        db.update(TABLE_CONTACTS, values, whereClause, args);
        db.close();
    }

    public Contact getContactById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = new String[]{String.valueOf(id)};
        Cursor cursor = db.rawQuery("SELECT * FROM contacts WHERE id = ?", args);
        Contact contact = new Contact();
        if (cursor != null && cursor.moveToFirst()) {
            contact.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            contact.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)));
            contact.setLastName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)));
            contact.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER)));
            contact.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
            cursor.close();
        }
        db.close();
        return contact;
    }

    public String normalizePhoneNumber(String phone) {
        String phoneNumber = phone.replaceAll("[\\s-]", "");
        if (phoneNumber.startsWith("+33"))
            return phoneNumber.replaceFirst("\\+33", "0");
        return phoneNumber;
    }

    public Contact getContactByPhoneNumber(String phoneNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        String normalizedPhoneNum = normalizePhoneNumber(phoneNumber);
        String[] args = new String[]{normalizedPhoneNum};
        String query = "SELECT * FROM contacts WHERE " + COLUMN_PHONE_NUMBER +" = ?";
        Cursor cursor = db.rawQuery(query, args);
        Contact contact = new Contact();
        if (cursor != null && cursor.moveToFirst()) {
            contact.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            contact.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)));
            contact.setLastName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)));
            contact.setNickname(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NICKNAME)));
            contact.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER)));
            contact.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
            cursor.close();
        }
        db.close();
        return contact;
    }

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<Contact>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM contacts", null);
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                contact.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)));
                contact.setLastName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)));
                contact.setNickname(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NICKNAME)));
                contact.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER)));
                contact.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
                contacts.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contacts;
    }

    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = new String[]{String.valueOf(contact.getId())};
        db.delete(TABLE_CONTACTS, "id = ?", args);
        db.close();
    }
}
