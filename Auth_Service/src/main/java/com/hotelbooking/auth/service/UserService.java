package com.hotelbooking.auth.service;

import java.util.List;

import com.hotelbooking.auth.dto.UserUpdateRequest;
import com.hotelbooking.auth.entity.Users;

public interface UserService {
	    Users findById(Long userId);
	    Users updateUser(Long userId, UserUpdateRequest request);
	    void deleteUser(Long userId);
	    List<Users> findAllUsers();
	}

