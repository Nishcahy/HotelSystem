package com.hotelbooking.auth.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hotelbooking.auth.dto.UserUpdateRequest;
import com.hotelbooking.auth.entity.Users;
import com.hotelbooking.auth.exeption.UserNotFound;
import com.hotelbooking.auth.repository.UserRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepo userRepo;

	@Override
	public Users findById(Long userId) {
		// Uses orElseThrow to handle the Optional result cleanly.
		return userRepo.findById(userId)
				.orElseThrow(() -> new UserNotFound("User not found with ID: " + userId));
	}

	@Override
	@Transactional
	public Users updateUser(Long userId, UserUpdateRequest request) {
		Users existingUser=userRepo.findById(userId).orElseThrow(()->new UserNotFound("User not found with ID:"+userId));
		
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

}
