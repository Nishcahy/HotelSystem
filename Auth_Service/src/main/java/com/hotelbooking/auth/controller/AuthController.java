package com.hotelbooking.auth.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotelbooking.auth.dto.AuthRequest;
import com.hotelbooking.auth.dto.RefreshTokenRequest;
import com.hotelbooking.auth.dto.RegistrationRequest;
import com.hotelbooking.auth.dto.TokenResponse;
import com.hotelbooking.auth.entity.RefreshToken;
import com.hotelbooking.auth.entity.Users;
import com.hotelbooking.auth.exeption.TokenRefreshException;
import com.hotelbooking.auth.service.AuthService;
import com.hotelbooking.auth.service.TokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	private final TokenService tokenService;
	private final AuthenticationManager authenticationManager;
	
	@PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest request) {
        try {
            authService.registerNewUser(request);
            return new ResponseEntity<>("User registered successfully.", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Catches UserAlreadyExistsException or role-related errors from AuthService
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // --- 2. User Login Endpoint ---
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticateUser(@Valid @RequestBody AuthRequest request) {
        
        // 1. Authenticate credentials using the AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 2. Set the authenticated user in the security context (optional, but good practice)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Get the User entity from the principal
        // The principal is the Users object because Users implements UserDetails
        Users user = (Users) authentication.getPrincipal();

        // 4. Generate Tokens
        String jwtAccessToken = tokenService.generateAccessToken(user);
        RefreshToken refreshToken = tokenService.createRefreshToken(user);
        
        // 5. Build and return the successful response
        return ResponseEntity.ok(new TokenResponse(
            jwtAccessToken, 
            refreshToken.getToken(), 
            refreshToken.getExpiry(), 
            user.getUserId()
        ));
    }

    // --- 3. Token Refresh Endpoint ---
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        
        String requestRefreshToken = request.getRefreshToken();

        // 1. Find the token in the database
        Optional<RefreshToken> optionalToken = tokenService.findByToken(requestRefreshToken);

        if (optionalToken.isEmpty()) {
             throw new TokenRefreshException(requestRefreshToken, "Refresh token is not in the database!");
        }

        RefreshToken refreshToken = optionalToken.get();
        
        // 2. Verify token expiry
        refreshToken = tokenService.verifyExpiration(refreshToken);

        // 3. Get the user associated with the valid refresh token
        Users user = refreshToken.getUser();

        // 4. Generate NEW Access and Refresh Tokens
        String newAccessToken = tokenService.generateAccessToken(user);
        
        // OPTIONAL: Revoke the old refresh token and issue a new one for rotation
        tokenService.deleteToken(refreshToken); 
        RefreshToken newRefreshToken = tokenService.createRefreshToken(user);

        // 5. Return new tokens
        return ResponseEntity.ok(new TokenResponse(
            newAccessToken, 
            newRefreshToken.getToken(), 
            newRefreshToken.getExpiry(), 
            user.getUserId()
        ));
    }
    
    // --- 4. User Logout Endpoint ---
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@Valid @RequestBody RefreshTokenRequest request) {
        
        // 1. Find the refresh token
        Optional<RefreshToken> optionalToken = tokenService.findByToken(request.getRefreshToken());
        
        // 2. If found, delete it (revoking the long-lived session)
        if (optionalToken.isPresent()) {
            tokenService.deleteToken(optionalToken.get());
        }
        
        // The JWT access token will expire naturally, but the refresh token is gone.
        return ResponseEntity.ok("User logged out successfully.");
    }
}
