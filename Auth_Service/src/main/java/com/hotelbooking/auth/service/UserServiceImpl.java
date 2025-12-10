package com.hotelbooking.auth.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hotelbooking.auth.dto.UserUpdateRequest;
import com.hotelbooking.auth.entity.Roles;
import com.hotelbooking.auth.entity.Users;
import com.hotelbooking.auth.exeption.UserNotFound;
import com.hotelbooking.auth.repository.RoleRepo;
import com.hotelbooking.auth.repository.UserRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepo userRepo;
	private final RoleRepo roleRepo;
	@Override
	public Users findById(Long userId) {
		// Uses orElseThrow to handle the Optional result cleanly.
		return userRepo.findById(userId).orElseThrow(() -> new UserNotFound("User not found with ID: " + userId));
	}

	@Override
	@Transactional
	public Users updateUser(Long userId, UserUpdateRequest request) {
		Users existingUser = userRepo.findById(userId)
				.orElseThrow(() -> new UserNotFound("User not found with ID:" + userId));

		existingUser.setAge(request.getAge());
		existingUser.setFullName(request.getFullName());
		existingUser.setPhone(request.getPhone());

		return userRepo.save(existingUser);
	}

	@Override
	@Transactional
	public void deleteUser(Long userId) {
		if (!userRepo.existsById(userId)) {
			throw new UserNotFound("User not found with ID: " + userId);
		}

		// 2. Delete the user
		userRepo.deleteById(userId);

	}

	@Override
	public List<Users> findAllUsers() {

		return userRepo.findAll();
	}

	@Override
	public Optional<Users> findByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	@Override
	public Users save(Users user) {

		return userRepo.save(user);
	}

	@Override
	public Users updateRoles(Long userId, Set<String> newRoleNames) {
		Users user = userRepo.findById(userId).orElseThrow(() -> new UserNotFound("User not found."));


		Set<Roles> newRoles = newRoleNames.stream()
				.map(roleName -> roleRepo.findByRole(roleName)
						.orElseThrow(() -> new RuntimeException("Role " + roleName + " not found.")))
				.collect(Collectors.toSet());

		
		user.setRoles(newRoles);

		return userRepo.save(user);

	}

}
