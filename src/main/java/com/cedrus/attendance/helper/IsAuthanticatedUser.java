package com.cedrus.attendance.helper;

import org.springframework.security.core.context.SecurityContextHolder;

import com.cedrus.attendance.entity.User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IsAuthanticatedUser {

	private static IsAuthanticatedUser authanticatedUser = new IsAuthanticatedUser();


	public static IsAuthanticatedUser getInstance() {
		return authanticatedUser;
	}

	public User isLoggedInUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
