package com.example.ft_hangouts;

import com.example.ft_hangouts.models.Contact;

import java.util.ArrayList;
import java.util.Objects;

public class ContactsSingleton {
    private static final ContactsSingleton instance = new ContactsSingleton();

    private final ArrayList<Contact> contacts = new ArrayList<>();
    public static ContactsSingleton getInstance() { return instance; }

    private ContactsSingleton() {
        contacts.add(new Contact("Alice", "Martin", "0123456789", "alice.martin@example.com", "Maison d'Alice", "75001"));
        contacts.add(new Contact("Bob", "Dupont", "0612345678", "bob.dupont@example.com", "Appartement de Bob", "69001"));
        contacts.add(new Contact("Claire", "Leroy", "0789123456", "claire.leroy@example.com", "Bureau de Claire", "33000"));
        contacts.add(new Contact("David", "Moreau", "0654321098", "david.moreau@example.com", "Maison de David", "75002"));
        contacts.add(new Contact("Eva", "Bernard", "0612345679", "eva.bernard@example.com", "Café d'Eva", "75003"));
        contacts.add(new Contact("François", "Garnier", "0798765432", "francois.garnier@example.com", "Studio de François", "59000"));
        contacts.add(new Contact("Sophie", "Rousseau", "0687654321", "sophie.rousseau@example.com", "Bureau de Sophie", "31000"));
        contacts.add(new Contact("Lucas", "Petit", "0623456789", "lucas.petit@example.com", "Maison de Lucas", "44000"));
        contacts.add(new Contact("Chloé", "Fournier", "0756789123", "chloe.fournier@example.com", "Appartement de Chloé", "13000"));
        contacts.add(new Contact("Thomas", "Lemoine", "0634567890", "thomas.lemoine@example.com", "Bureau de Thomas", "67000"));
        contacts.add(new Contact("Julien", "Boucher", "0612345670", "julien.boucher@example.com", "Maison de Julien", "75004"));
        contacts.add(new Contact("Laura", "Giraud", "0787654321", "laura.giraud@example.com", "Appartement de Laura", "59001"));
        contacts.add(new Contact("Nicolas", "Blanc", "0656789123", "nicolas.blanc@example.com", "Bureau de Nicolas", "31001"));
        contacts.add(new Contact("Camille", "Dufour", "0623456780", "camille.dufour@example.com", "Maison de Camille", "44001"));
        contacts.add(new Contact("Antoine", "Lemoine", "0754321098", "antoine.lemoine@example.com", "Café d'Antoine", "13001"));
        contacts.add(new Contact("Manon", "Charpentier", "0634567891", "manon.charpentier@example.com", "Bureau de Manon", "67001"));
        contacts.add(new Contact("Victor", "Gauthier", "0612345671", "victor.gauthier@example.com", "Maison de Victor", "75005"));
        contacts.add(new Contact("Inès", "Renaud", "0789123457", "ines.renaud@example.com", "Appartement d'Inès", "69002"));
        contacts.add(new Contact("Paul", "Leroux", "0654321099", "paul.leroux@example.com", "Bureau de Paul", "33001"));
        contacts.add(new Contact("Léa", "Pichon", "0687654322", "lea.pichon@example.com", "Maison de Léa", "44002"));
    }

    public ArrayList<Contact> getContacts() { return this.contacts; }

    public Contact getContact(String id) {
       for (Contact contact : contacts) {
           if (Objects.equals(contact.getId(), id)) {
               return contact;
           }
       }
       return null;
    }

    public ArrayList<Contact> filterContact(String[] query) {
       if (query.length == 0) return this.getContacts();
       ArrayList<Contact> contactReq = new ArrayList<>();
       for (Contact contact : contacts) {
           for (String word : query) {
               if (contact.getFirstName().toLowerCase().contains(word.toLowerCase())
                       || contact.getLastName().toLowerCase().contains(word.toLowerCase())
                       || contact.getPhone().toLowerCase().contains(word.toLowerCase())) {
                   contactReq.add(contact);
                   break;
               }
           }
       }
       return contactReq;
    }

}
