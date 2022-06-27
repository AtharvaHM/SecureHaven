package com.app.securehaven.auth;

public class User {
    public String name, contact, email, phoneInfo;

    public User() {

    }

    public User(String name, String contact, String email, String phoneInfo) {
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.phoneInfo = phoneInfo;
    }
}
