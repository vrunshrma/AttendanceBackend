package com.cedrus.attendance.service;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.cedrus.attendance.common.Constants;
import com.cedrus.attendance.common.Messages;
import com.cedrus.attendance.entity.User;
import com.cedrus.attendance.exceptions.UserIsNotFoundException;
import com.cedrus.attendance.exceptions.UserNotAutharizedException;
import com.cedrus.attendance.repository.UserRepository;
import com.cedrus.attendance.request.LoginRequest;
import com.cedrus.attendance.response.APIResponse;
import com.cedrus.attendance.response.JwtResponse;
import com.cedrus.attendance.utility.JwtUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService implements IAuthService {

	private JwtUtils jwtUtils;

	private UserRepository userRepository;

	private AuthenticationManager authenticationManager;

	@Override
	public JwtResponse login(@Valid LoginRequest loginRequest) {
		log.info("AuthService :: User authentication start...");
		JwtResponse response = null;
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			response = jwtUtils.generateJwtToken(authentication, loginRequest, "general_access");
			log.info("User is authenticated.....");
		} catch (Exception e) {
			throw new UserNotAutharizedException("Invalid Email/Mobile or password...!");
		}
		return response;
	}

	@Override
	public APIResponse<User> fetchByUsername(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		if (!user.isPresent()) {
			throw new UserIsNotFoundException(Messages.USER_IS_NOT_FOUND_WITH_USERNAME + username);
		}
		return APIResponse.<User>builder().status(Constants.SUCCESS.getValue())
				.message(Messages.DATA_FETCHED_SUCCESSFULLY).response(user.get()).build();
	}

	@Override
	public APIResponse<User> getUserFromJwtToken(String token) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			log.info("User authentication username: {}", ((UserDetails) principal).getUsername());
			Optional<User> user = userRepository.findByUsername(((UserDetails) principal).getUsername());
			if (user.isPresent()) {
				return APIResponse.<User>builder().status(Constants.SUCCESS.getValue())
						.message(Messages.DATA_FETCHED_SUCCESSFULLY).response(user.get()).build();
			}
		}
		throw new UserIsNotFoundException(Messages.USERNAME_NOT_FOUND);
	}

	@Override
	public String getUsername() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		}
		return principal.toString();
	}

}
