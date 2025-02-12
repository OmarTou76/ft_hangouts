package com.example.ft_hangouts.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ft_hangouts.R;
import com.example.ft_hangouts.database.MessageDatabaseHelper;
import com.example.ft_hangouts.models.Contact;
import com.example.ft_hangouts.models.Message;
import com.example.ft_hangouts.utils.AToolbar;
import com.example.ft_hangouts.database.ContactDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MessagesActivity extends AToolbar {
    private static MessagesActivity instance;

    public static boolean isActive;
    ContactDatabaseHelper contactStore;
    MessageDatabaseHelper messageStore;

    List<Message> messages;

    Contact contact;

    FloatingActionButton fab;

    ScrollView messageContainer;

    EditText textMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;
        isActive = true;

        fab = findViewById(R.id.send_message);
        contactStore = new ContactDatabaseHelper(this);
        messageStore = new MessageDatabaseHelper(this);
        messageContainer = findViewById(R.id.message_container);
        textMessage = findViewById(R.id.text_message);
        fetchContact();
        initSendButton();

    }

    @Override
    protected void onResume() {
        super.onResume();
        fab.setBackgroundTintList(ColorStateList.valueOf(getSavedHeaderColor()));

        EditText v = findViewById(R.id.text_message);
        if (!v.getHint().toString().equals(getString(R.string.text_message))) {
            recreate();
        }
        fetchContact();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActive = false;
    }
    public static MessagesActivity getInstance() {
        return instance;
    }

    public void handleNewMessage(String sender, String content) {
        if (contact != null && contact.getPhone().equals(sender)) {
            Message message = new Message(contact.getId(), true, content);
            runOnUiThread(() -> appendMessageToUI(message));
            messageContainer.post(() -> messageContainer.fullScroll(View.FOCUS_DOWN));
        }
    }
    @Override
    protected int getLayoutResource() { return R.layout.activity_messages; }

    @Override
    protected String getActivityTitle() {
        return "Contact name";
    }

    private void fetchContact() {
        int id = getIntent().getIntExtra("ID", -1);
        if (id != -1) {
            this.contact = contactStore.getContactById(id);
            if (this.contact == null) {
                Toast.makeText(this, "Contact not found", Toast.LENGTH_SHORT).show();
                finish();
            }
            else {
                this.messages = messageStore.getMessagesByContactId(this.contact);
                LinearLayout parent =  findViewById(R.id.message_layout);
                parent.removeAllViews();
                for (Message message : messages) {
                    appendMessageToUI(message);
                }
                setToolbarTitle(String.format("%s %s", this.contact.getFirstName(), this.contact.getLastName()));
                messageContainer.post(() -> messageContainer.fullScroll(View.FOCUS_DOWN));
            }
        } else {
            Toast.makeText(this, "ID not provided", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }

    private void appendMessageToUI(Message message) {
        LinearLayout parent = findViewById(R.id.message_layout);

        TextView messageBlock = new TextView(this);
        messageBlock.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        messageBlock.setBackgroundResource(R.drawable.edit_text_border);
        messageBlock.setPadding(
                dpToPx(12), dpToPx(6), dpToPx(12), dpToPx(6)
        );
        messageBlock.setText(message.getContent());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        if (message.isSender()) {
            params.gravity = Gravity.START;
            params.setMargins(0, 0, dpToPx(64), dpToPx(12));
        } else {
            params.gravity = Gravity.END;
            params.setMargins(dpToPx(64), 0, 0, dpToPx(12));
            messageBlock.setBackgroundTintList(ColorStateList.valueOf(getSavedHeaderColor()));
            messageBlock.setTextColor(getColor(R.color.white));
        }
        messageBlock.setLayoutParams(params);
        parent.addView(messageBlock);
    }

    private void initSendButton() {
        fab.setBackgroundTintList(ColorStateList.valueOf(getSavedHeaderColor()));
        fab.setOnClickListener(v -> {
            if (textMessage.getText().toString().isEmpty())
            {
                Toast.makeText(this, R.string.empty_message, Toast.LENGTH_SHORT).show();
                return;
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
            } else
                sendSMS();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMS();
            } else {
                Toast.makeText(this, "Permission denied to send SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendSMS() {
        try {
            Message message = new Message(contact.getId(), false, textMessage.getText().toString());
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(contact.getPhone(), null, message.getContent(), null, null);
            messageStore.addMessage(message);
            appendMessageToUI(message);
            textMessage.setText("");
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
