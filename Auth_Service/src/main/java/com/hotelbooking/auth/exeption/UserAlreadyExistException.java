package com.hotelbooking.auth.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UserAlreadyExistException extends RuntimeException {
	
	private String msg;
	public UserAlreadyExistException(String msg){
		super(msg);
		this.msg=msg;
	}
}
