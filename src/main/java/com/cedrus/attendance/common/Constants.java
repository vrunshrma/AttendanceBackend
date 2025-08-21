package com.cedrus.attendance.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Constants {

	AUTHORIZATION("Authorization"), SUCCESS("SUCCESS"), FAILED("FAILED"), ERROR("NETWORK ERROR");

	private final String value;

}
