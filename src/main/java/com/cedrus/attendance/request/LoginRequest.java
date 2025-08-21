package com.cedrus.attendance.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	@NotBlank
	private String username;
	@NotBlank
	private String password;
	private boolean rememberMe;

}
