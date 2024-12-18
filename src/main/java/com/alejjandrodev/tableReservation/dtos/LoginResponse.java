package com.alejjandrodev.tableReservation.dtos;




public class LoginResponse {
    private String accessToken;

    public LoginResponse( String accessToken) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
    }

    private String tokenType = "Bearer";
}
