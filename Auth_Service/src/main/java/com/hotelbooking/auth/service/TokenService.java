package com.hotelbooking.auth.service;

import java.util.Optional;

import com.hotelbooking.auth.entity.RefreshToken;
import com.hotelbooking.auth.entity.Users;

public interface TokenService {
	String generateAccessToken(Users user);

	RefreshToken createRefreshToken(Users user);

	Optional<RefreshToken> findByToken(String token);

	RefreshToken verifyExpiration(RefreshToken token);

	void deleteToken(RefreshToken token);
}
