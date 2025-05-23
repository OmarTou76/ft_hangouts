package com.example.ft_hangouts.models;

public class Contact {
    private int id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;

    private String nickname;

    public Contact() {
        this.id = -1;
    }
    public Contact(String firstName, String lastName, String phone, String email, String nickname) {
           this.id = -1;
           this.firstName = firstName;
           this.lastName = lastName;
           this.phone = phone;
           this.email = email;
           this.nickname = nickname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() { return this.id; }
    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }
    public String getPhone() { return this.phone; }
    public String getEmail() { return this.email; }

    public String getNickname() { return this.nickname; }

}
