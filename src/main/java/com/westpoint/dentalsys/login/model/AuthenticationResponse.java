package com.westpoint.dentalsys.login.model;

public class AuthenticationResponse {
    private String token;
    private String message;
    private String role;

    private String refreshToken;

    public AuthenticationResponse(String token, String refreshToken, String message, String role) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.message = message;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

    public String getRole() {
        return role;
    }
}
