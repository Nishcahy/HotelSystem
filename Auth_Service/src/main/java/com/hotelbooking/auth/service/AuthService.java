package com.hotelbooking.auth.service;

import com.hotelbooking.auth.dto.RegistrationRequest;
import com.hotelbooking.auth.entity.Users;

public interface AuthService {
	Users registerNewUser(RegistrationRequest request);
	
	

}
