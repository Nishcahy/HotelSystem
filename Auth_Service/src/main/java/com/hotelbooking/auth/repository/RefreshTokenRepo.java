package com.hotelbooking.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotelbooking.auth.entity.RefreshToken;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long>  {
	Optional<RefreshToken> findByToken(String token);
}
