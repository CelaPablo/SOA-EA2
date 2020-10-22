package com.example.soa_ea2;

public class User {

    private String email, token, tokenRefresh;

    public User(String email, String token, String tokenRefresh) {
        this.email = email;
        this.token = token;
        this.tokenRefresh = tokenRefresh;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public String getTokenRefresh() {
        return tokenRefresh;
    }

    public void setTokenRefresh(String tokenRefresh) {
        this.tokenRefresh = tokenRefresh;
    }
}
