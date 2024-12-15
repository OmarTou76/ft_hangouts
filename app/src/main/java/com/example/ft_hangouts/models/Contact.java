package com.example.ft_hangouts.models;

import java.util.UUID;

public class Contact {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String phone;
    private final String email;
    private final String address;
    private final String postalCode;

    public Contact(String firstName, String lastName, String phone, String email, String address, String postalCode) {
           this.id = UUID.randomUUID().toString();
           this.firstName = firstName;
           this.lastName = lastName;
           this.phone = phone;
           this.email = email;
           this.address = address;
           this.postalCode = postalCode;
    }

    public String getId() { return this.id; }
    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }
    public String getPhone() { return this.phone; }
    public String getEmail() { return this.email; }
    public String getAddress() { return this.address; }
    public String getPostalCode() { return this.postalCode; }
}
