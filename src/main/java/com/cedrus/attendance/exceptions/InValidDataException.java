package com.cedrus.attendance.exceptions;

public class InValidDataException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InValidDataException(String msg) {
		super(msg);
	}

}