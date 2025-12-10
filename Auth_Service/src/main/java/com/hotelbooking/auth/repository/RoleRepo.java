package com.hotelbooking.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotelbooking.auth.entity.Roles;

public interface RoleRepo extends JpaRepository<Roles, Long> {
	Optional<Roles> findByRole(String role);
}
