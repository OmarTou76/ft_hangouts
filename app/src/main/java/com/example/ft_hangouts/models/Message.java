package com.example.ft_hangouts.models;

public class Message {
    private int id;

    private int contactId;

    private boolean isSender;
    private String content;

    public Message(int contactId, boolean isSender, String content) {
        setContactId(contactId);
        setContent(content);
        setSender(isSender ? 1 : 0);
    }

    public Message() {}

    public void setId(int id) {
        this.id = id;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public boolean isSender() {
        return isSender;
    }

    public void setSender(int sender) {
        isSender = sender == 1;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }
}
