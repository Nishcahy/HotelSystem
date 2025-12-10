package com.hotelbooking.auth.dto;

import java.time.Instant;

import lombok.Data;
@Data
public class TokenResponse {
	private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer"; 
    private Instant expiryDate;
    private Long userId; 

    public TokenResponse(String accessToken, String refreshToken, Instant expiryDate, Long userId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiryDate = expiryDate;
        this.userId = userId;
    }
}
