package com.cedrus.attendance.helper;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cedrus.attendance.bean.UserRegistration;
import com.cedrus.attendance.common.Messages;
import com.cedrus.attendance.entity.User;
import com.cedrus.attendance.repository.UserRepository;
import com.cedrus.attendance.response.ErrorResponse;
import com.cedrus.attendance.utility.SecurityUtility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserValidationHelper {

	@Autowired
	private UserRepository userRepository;

	public void validateUser(UserRegistration userRegistration, List<ErrorResponse> list) {
		if(userRegistration.getMobile() == null || userRegistration.getMobile().isEmpty()) {
			validatePasswordForEmail(userRegistration,list);
			validatePassword(userRegistration, list);
		}else {
			validateName(userRegistration, list);
			validateEmail(userRegistration, list);
			validatePassword(userRegistration, list);
			validateMobile(userRegistration, list);
		}
	}

	public void validateName(UserRegistration userRegistration, List<ErrorResponse> list) {
		if (!isValidName(userRegistration.getFirstName())) {
			list.add(ErrorResponse.builder().field("name").errorMessage(Messages.NAME_IS_NOT_VALID).build());
		}
	}

	public void validatePassword(UserRegistration userRegistration, List<ErrorResponse> list) {
		if (userRegistration.getPassword().equals(userRegistration.getConfirmPassword())) {
			userRegistration.setPassword(SecurityUtility.passwordEncoder().encode(userRegistration.getPassword()));
		} else {
			list.add(ErrorResponse.builder().field("password").errorMessage(Messages.PASSWORD_IS_NOT_SAME).build());
		}
	}
	
	public void updateResetpassword(UserRegistration userRegistration, List<ErrorResponse> list) {
		
	}
	
	public void validateEmail(UserRegistration userRegistration, List<ErrorResponse> list) {
		userRepository.findByUsername(userRegistration.getUsername()).ifPresent(user -> {
			list.add(ErrorResponse.builder().field("email")
					.errorMessage(Messages.EMAIL_EXIST + " : " + user.getUsername()).build());
		});
	}
	
	public void validatePasswordForEmail(UserRegistration userRegistration, List<ErrorResponse> list) {
		Optional<User> userOptional = userRepository.findByUsername(userRegistration.getUsername());
		if (!userOptional.isPresent()) {
	        list.add(ErrorResponse.builder()
	            .field("email")
	            .errorMessage(Messages.EMAIL_NOT_EXIST + " : " + userRegistration.getUsername())
	            .build());
	    }
	}


	public void validateMobile(UserRegistration userRegistration, List<ErrorResponse> list) {
		if(isMobileValid(userRegistration.getMobile())) {
			userRepository.findByMobile(userRegistration.getMobile()).ifPresent(user -> {
				list.add(ErrorResponse.builder().field("mobile")
						.errorMessage(Messages.MOBILE_EXIST + " : " + user.getMobile()).build());
			});
		}else {
			list.add(ErrorResponse.builder().field("mobile").errorMessage(Messages.MOBILE_IS_NOT_VALID).build());
		}
	}

	public static boolean isMobileValid(String mobile) {
		Pattern p = Pattern.compile("^\\d{10}$");
		Matcher m = p.matcher(mobile);
		return m.matches();
	}

	public static boolean isValidName(String name) {
		Pattern p = Pattern.compile("^[\\p{L} .'-]+$");
		Matcher m = p.matcher(name);
		return m.matches();
	}

}
