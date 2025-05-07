package com.example.ft_hangouts.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.ft_hangouts.activities.MessagesActivity;
import com.example.ft_hangouts.database.ContactDatabaseHelper;
import com.example.ft_hangouts.database.MessageDatabaseHelper;
import com.example.ft_hangouts.models.Contact;
import com.example.ft_hangouts.models.Message;

import java.util.Objects;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
       if (Objects.equals(intent.getAction(), "android.provider.Telephony.SMS_RECEIVED")) {
           Bundle bundle = intent.getExtras();
           if (bundle != null) {
               Object[] pdus = (Object[]) bundle.get("pdus");
               if (pdus != null) {
                   for (Object pdu : pdus) {
                       SmsMessage smsMessage;
                       String format = bundle.getString("format");
                       smsMessage = SmsMessage.createFromPdu((byte[]) pdu, format);
                       String sender = smsMessage.getDisplayOriginatingAddress();
                       String messageBody = smsMessage.getMessageBody();

                       saveMessageToDatabase(context, sender, messageBody);
                       if (MessagesActivity.isActive) {
                           MessagesActivity.getInstance().handleNewMessage(sender, messageBody);
                       }
                   }
               }
           }
       }
    }
    private void saveMessageToDatabase(Context context, String sender, String messageBody) {
        try (MessageDatabaseHelper dbHelper = new MessageDatabaseHelper(context); ContactDatabaseHelper contactHelper = new ContactDatabaseHelper(context)) {
            Contact contact = contactHelper.getContactByPhoneNumber(sender);

            if (contact == null || contact.getPhone().isEmpty()) {
                Log.d("Testerie: ", "Contact not found for phone number: " + sender);
                return;
            }

            Message message = new Message(contact.getId(), true, messageBody);

            dbHelper.addMessage(message);
            Log.d("Testerie: ", "Message saved to database for contact: " + contact.getPhone());
        } catch (Exception e) {
            Log.e("Testerie: ", "Error saving message to database", e);
        }
    }
}
