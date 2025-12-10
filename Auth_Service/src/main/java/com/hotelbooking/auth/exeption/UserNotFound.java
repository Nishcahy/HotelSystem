package com.hotelbooking.auth.exeption;

public class UserNotFound extends RuntimeException {
	
	public UserNotFound(String msg) {
		super(msg);
	}

}
