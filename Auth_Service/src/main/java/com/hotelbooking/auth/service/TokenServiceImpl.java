package com.hotelbooking.auth.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.hotelbooking.auth.entity.RefreshToken;
import com.hotelbooking.auth.entity.Users;
import com.hotelbooking.auth.exeption.TokenRefreshException;
import com.hotelbooking.auth.repository.RefreshTokenRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
	private final JwtEncoder jwtEncoder;
    private final RefreshTokenRepo refreshTokenRepository;

    @Value("${application.security.jwt.access-token-expiration-minutes:15}")
    private long accessTokenExpirationMinutes;
    
    @Value("${application.security.jwt.refresh-token-expiration-days:7}")
    private long refreshTokenExpirationDays;

   
    
    // Assumes RefreshToken, Users entities and TokenService interface are defined

    @Override
    public String generateAccessToken(Users user) {
        Instant now = Instant.now();
        
        // Collect user roles (authorities) into a space-separated string for the 'scope' claim
        String scope = user.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(accessTokenExpirationMinutes * 60)) 
                .subject(user.getUsername()) 
                .claim("scope", scope) 
                .claim("userId", user.getUserId())
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public RefreshToken createRefreshToken(Users user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        
        // Refresh token expires in days defined in application.yml
        refreshToken.setExpiry(Instant.now().plusSeconds(refreshTokenExpirationDays * 24 * 60 * 60));
        
        // Generate a cryptographically secure random UUID string for the token
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiry().isBefore(Instant.now())) {
            // Delete the expired token and throw exception
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please log in again.");
        }
        return token;
    }

    @Override
    public void deleteToken(RefreshToken token) {
        refreshTokenRepository.delete(token);
    }
}
