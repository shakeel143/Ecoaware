package com.example.ecoguardians.Model;

public class User {
    public String name, email, language;

    public User() {}  // Needed for Firebase

    public User(String name, String email, String language) {
        this.name = name;
        this.email = email;
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
