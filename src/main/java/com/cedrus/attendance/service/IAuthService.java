package com.cedrus.attendance.service;

import javax.validation.Valid;

import com.cedrus.attendance.request.LoginRequest;
import com.cedrus.attendance.response.APIResponse;
import com.cedrus.attendance.response.JwtResponse;
import com.cedrus.attendance.entity.User;

public interface IAuthService {

	JwtResponse login(@Valid LoginRequest loginRequest);

	APIResponse<User> fetchByUsername(String username);

	APIResponse<User> getUserFromJwtToken(String token);
	
	String getUsername();

}
