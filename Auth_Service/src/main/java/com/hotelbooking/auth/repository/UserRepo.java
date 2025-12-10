package com.hotelbooking.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.hotelbooking.auth.entity.Users;

public interface UserRepo extends JpaRepository<Users, Long> {
	Optional<Users> findByEmail(String email);
}
