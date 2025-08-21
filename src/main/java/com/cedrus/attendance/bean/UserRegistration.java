package com.cedrus.attendance.bean;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.cedrus.attendance.config.ValidPassword;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistration implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Size(min = 3, max = 25, message = "Please enter valid firstname.")
	private String firstName;
	@NotNull
	@Size(min = 3, max = 25, message = "Please enter valid lastname.")
	private String lastName;
	@NotNull
	@Email(message = "Please enter valid email.")
	private String username;
	@NotNull
	@Size(min = 10, max = 10, message = "Please enter valid mobile.")
	private String mobile;
	@NotNull
	@Size(min = 6 , max = 20)
	@ValidPassword
	private transient String password;
//	@NotNull(message = "message required")
	private Date createDate;
	@NotNull
	@Size(min = 6 , max = 20)
	private transient String confirmPassword;
	private String role;
	@NotNull(message = "Address is required.")
	private String address;
	private boolean isUsing2FA;
	@NotNull
	private String department;

}
