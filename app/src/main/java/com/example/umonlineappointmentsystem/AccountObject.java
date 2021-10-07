package com.example.umonlineappointmentsystem;

public class AccountObject {
    private String id, displayName, email, expiration, umindanaoAccount;

    public AccountObject() {
    }

    public AccountObject(String id, String displayName, String email, String expiration, String umindanaoAccount) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.expiration = expiration;
        this.umindanaoAccount = umindanaoAccount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getUmindanaoAccount() {
        return umindanaoAccount;
    }

    public void setUmindanaoAccount(String umindanaoAccount) {
        this.umindanaoAccount = umindanaoAccount;
    }
}
