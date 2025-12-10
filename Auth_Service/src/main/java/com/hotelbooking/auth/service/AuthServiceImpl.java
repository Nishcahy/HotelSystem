package com.hotelbooking.auth.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hotelbooking.auth.dto.RegistrationRequest;
import com.hotelbooking.auth.entity.Roles;
import com.hotelbooking.auth.entity.Users;
import com.hotelbooking.auth.exeption.UserAlreadyExistException;
import com.hotelbooking.auth.repository.RoleRepo;
import com.hotelbooking.auth.repository.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	
	private final UserRepo userRepo;
	private final RoleRepo roleRepo;
	private final PasswordEncoder passwordEncoder;
	
	public Users registerNewUser(RegistrationRequest request) {

		

		// 1. Check if user already exists
		if (userRepo.findByEmail(request.getEmail()).isPresent()) {
			throw new UserAlreadyExistException("User with email " + request.getEmail() + " already exists.");
		}

		// 2. Load the default role (ROLE_CUSTOMER)
		// Ensure this role is pre-loaded in your 'role' table on application startup
		Roles defaultRole = roleRepo.findByRole("ROLE_CUSTOMER").orElseThrow(()->new RuntimeException("Role not found"));
		// 3. Map DTO to Entity and Hash Password
		Users user = new Users();
		user.setEmail(request.getEmail());
		user.setFullName(request.getFullName());
		user.setPhone(request.getPhone());
		user.setAge(request.getAge());

		user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

		Set<Roles> roles = new HashSet<>();
		roles.add(defaultRole);
		user.setRoles(roles);

		// 4. Save the user
		return userRepo.save(user);
	}
}
