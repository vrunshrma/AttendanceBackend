package com.cedrus.attendance.exceptions;

public class UserNotAutharizedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserNotAutharizedException(String msg) {
		super(msg);
	}
}
