package com.sibola.app;

import java.io.Serializable;

/**
 * Created by Aizen on 3 Mei 2017.
 */

public class User implements Serializable{

    private String username;
    private String email;
    private String userId;
    private boolean member;
    private long deposit;

    public User(String username, String email, String userId) {
        this.username = username;
        this.email = email;
        this.userId = userId;
        this.deposit = 0;
        this.member = false;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isMember() {
        return member;
    }

    public void setMember(boolean member) {
        this.member = member;
    }

    public long getDeposit() {
        return deposit;
    }

    public void setDeposit(long deposit) {
        this.deposit = deposit;
    }
}
