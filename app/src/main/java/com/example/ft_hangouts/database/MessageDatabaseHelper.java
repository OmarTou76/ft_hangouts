package com.example.ft_hangouts.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ft_hangouts.models.Contact;
import com.example.ft_hangouts.models.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "message.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_MESSAGES = "messages";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CONTACT_ID = "contact_id";

    private static final String COLUMN_IS_SENDER = "is_sender";
    private static final String COLUMN_CONTENT = "content";

    public MessageDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_MESSAGES + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CONTACT_ID + " INTEGER, "
                + COLUMN_IS_SENDER + " INTEGER, "
                + COLUMN_CONTENT + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        onCreate(db);
    }

    public void addMessage(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_CONTACT_ID, message.getContactId());
        values.put(COLUMN_IS_SENDER, message.isSender() ? 1 : 0);
        values.put(COLUMN_CONTENT, message.getContent());
        db.insert(TABLE_MESSAGES,null, values);
        db.close();
    }

    public List<Message> getMessagesByContactId(Contact contact) {
        List<Message> messagesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] args = new String[]{String.valueOf(contact.getId())};
        String query = "SELECT * FROM " + TABLE_MESSAGES + " WHERE " + COLUMN_CONTACT_ID + " = ?;";
        Cursor cursor = db.rawQuery(query, args);

        while (cursor.moveToNext()) {
            Message message = new Message();
            message.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            message.setContactId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CONTACT_ID)));
            message.setContent(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)));
            message.setSender(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_SENDER)));
            messagesList.add(message);
        }
        cursor.close();
        db.close();

        return messagesList;
    }
}
