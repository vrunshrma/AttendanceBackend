package com.cedrus.attendance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.cedrus.attendance.common.Messages;
import com.cedrus.attendance.entity.User;
import com.cedrus.attendance.exceptions.UserIsNotFoundException;
import com.cedrus.attendance.exceptions.UserNotAutharizedException;
import com.cedrus.attendance.repository.UserRepository;


@Service
public class UserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UserIsNotFoundException {
		User user = userRepository.findByUsernameOrMobile(username, "Y", "N");
		if (user != null) {
			return user;
		}
		throw new UserNotAutharizedException(Messages.UNAUTHORIZED_USER);
	}
}
