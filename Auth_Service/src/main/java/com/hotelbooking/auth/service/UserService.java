package com.hotelbooking.auth.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.hotelbooking.auth.dto.UserUpdateRequest;
import com.hotelbooking.auth.entity.Users;

public interface UserService {
	    Users findById(Long userId);
	    Optional<Users> findByEmail(String email);
	    Users updateUser(Long userId, UserUpdateRequest request);
	    void deleteUser(Long userId);
	    List<Users> findAllUsers();
	    Users save(Users user);
	    Users updateRoles(Long userId, Set<String> newRoleNames);
	}

