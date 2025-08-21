package com.cedrus.attendance.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;

import com.cedrus.attendance.bean.UserBean;
import com.cedrus.attendance.bean.UserRegistration;
import com.cedrus.attendance.common.Constants;
import com.cedrus.attendance.common.Messages;
import com.cedrus.attendance.entity.PasswordResetToken;
import com.cedrus.attendance.entity.Role;
import com.cedrus.attendance.entity.RoleType;
import com.cedrus.attendance.entity.User;
import com.cedrus.attendance.entity.UserRole;
import com.cedrus.attendance.entity.VerificationToken;
import com.cedrus.attendance.exceptions.UserAlreadyExistException;
import com.cedrus.attendance.helper.UserValidationHelper;
import com.cedrus.attendance.repository.PasswordResetTokenRepository;
import com.cedrus.attendance.repository.RoleRepository;
import com.cedrus.attendance.repository.UserRepository;
import com.cedrus.attendance.repository.VerificationTokenRepository;
import com.cedrus.attendance.response.APIResponse;
import com.cedrus.attendance.response.ErrorResponse;
import com.cedrus.attendance.utility.SecurityUtility;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements IUserService {

	private UserRepository userRepository;

	private ModelMapper modelMapper;

	private RoleRepository roleRepository;

	private PasswordResetTokenRepository passwordTokenRepository;

	private UserValidationHelper validationHelper;


	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private VerificationTokenRepository tokenRepository;

	@Override
	public APIResponse<Object> createUser(UserRegistration userRegistration) {
		log.info("Inside UserService user registeration start...");
		List<ErrorResponse> errorResponse = validateUser(userRegistration);
		if (errorResponse.isEmpty()) {
			saveUser(userRegistration);
			log.info("Inside UserService user registered sucessfully...");
			return APIResponse.<Object>builder().status(Constants.SUCCESS.getValue()).message(Messages.USER_CREATED)
					.build();
		}
		return APIResponse.<Object>builder().status(String.valueOf(Constants.FAILED.getValue())).errors(errorResponse)
				.message(Messages.USER_VALIDATION_FAILED).build();
	}

	private void saveUser(UserRegistration userRegistration) {
	    log.info("Inside UserService saving user...");

	    // Map the user registration to a User entity
	    User user = modalMapper(userRegistration);

	    // Create a new Role and set its properties based on the user registration
	    Role role = new Role();
	    setRoles(userRegistration, role);

	    // Create a Set to store the UserRole mapping
	    Set<UserRole> userRole = new HashSet<>();
	    userRole.add(new UserRole(user, role));

	    // Log the roles that are being saved
	    userRole.forEach(roles -> log.info("Saving user role: " + roles.toString()));

	    // Save the role to the role repository
	    roleRepository.save(role);

	    // Add the user roles to the user's list of roles
	    user.getUserRoles().addAll(userRole);

	    // Save the user entity to the user repository
	    userRepository.save(user);
	}


	private void setRoles(UserRegistration userRegistration, Role role) {
		if (userRegistration.getRole().equalsIgnoreCase("ADMIN")) {
			role.setRoleId(1L);
			role.setName(RoleType.ADMIN.toString());
		} else if (userRegistration.getRole().equalsIgnoreCase("EMPLOYEE")) {
			role.setRoleId(2L);
			role.setName(RoleType.EMPLOYEE.toString());
		}
	}

	private User modalMapper(UserRegistration userRegistration) {
		userRegistration.setCreateDate(new Date());;
		User user = modelMapper.map(userRegistration, User.class);
		user.setCreateDate(userRegistration.getCreateDate());
		return user;
	}

	private User userBeanModalMapper(UserBean userBean) {
		User user = modelMapper.map(userBean, User.class);
		user.setCreateDate(new Date());
		return user;
	}

	private List<ErrorResponse> fetchOldPassword(UserRegistration userRegistration, List<ErrorResponse> list) {
		userRepository.findByUsername(userRegistration.getUsername()).ifPresent(user -> {
			String encodedPassword = user.getPassword();
			String providedPassword = userRegistration.getPassword();
			if (SecurityUtility.passwordEncoder().matches(providedPassword, encodedPassword)) {
				list.add(ErrorResponse.builder().field("password").errorMessage(Messages.PREVIOUS_PASSWORD).build());
			}
		});
		return list;
	}

	private List<ErrorResponse> validateUser(UserRegistration userRegistration) {
		log.info("Inside UserService validating user...");
		List<ErrorResponse> list = new ArrayList<>();
		validationHelper.validateUser(userRegistration, list);
		list = fetchOldPassword(userRegistration, list);
		return list;
	}

	@Override
	public APIResponse<List<UserBean>> findAllUser() {
		List<UserBean> userData = userRepository.findAll().stream().map(user -> modelMapper.map(user, UserBean.class))
				.collect(Collectors.toList());
		return APIResponse.<List<UserBean>>builder().status(Constants.SUCCESS.getValue()).response(userData)
				.message(Messages.DATA_FETCHED_SUCCESSFULLY).build();
	}

	@Override
	public APIResponse<UserBean> findByEmployeeId(String employeeId) {
		User user = userRepository.findById(Long.parseLong(employeeId)).orElseThrow(null);
		if (user != null) {
			return APIResponse.<UserBean>builder().status(Constants.SUCCESS.getValue())
					.response(modelMapper.map(user, UserBean.class)).message(Messages.DATA_FETCHED_SUCCESSFULLY)
					.build();
		}
		return APIResponse.<UserBean>builder().status(Constants.FAILED.getValue()).message(Messages.DATA_NOT_FOUND)
				.build();
	}

	@Override
	public APIResponse<String> updateUser(UserBean user) {

		return null;
	}

	private void updateUserPassword(UserRegistration userRegistration) {
		Optional<User> userOptional = userRepository.findByUsername(userRegistration.getUsername());
		User user = userOptional.get();
		user.setPassword(userRegistration.getPassword());
		userRepository.save(user);
	}

	@Override
	public APIResponse<Object> updatePassword(UserRegistration userRegistration) {
		log.info("Inside UserService updatePassword request start...");

		List<ErrorResponse> errorResponse = validateUser(userRegistration);
		if (errorResponse.isEmpty()) {
			updateUserPassword(userRegistration);
			log.info("Inside UserService user password updated sucessfully...");
			return APIResponse.<Object>builder().status(Constants.SUCCESS.getValue()).message(Messages.PASSWORD_UPDATED)
					.build();
		}
		return APIResponse.<Object>builder().status(String.valueOf(Constants.FAILED.getValue())).errors(errorResponse)
				.message(Messages.USER_VALIDATION_FAILED).build();
	}
	
	@Override
	public List<User> findUsersByEmployeeIds(List<String> employeeIDs) {
		// TODO Auto-generated method stub
		
		List<Long> employeeIdsLong = employeeIDs.stream()
                .map(Long::valueOf)   // Convert each String to Long
                .collect(Collectors.toList());
		
		List<User> userList = userRepository.findUsersByEmployeeIds(employeeIdsLong);
		for(User user : userList) {
			log.info("Users : "+user);
		}
		return userList;
	}

//---------------------------------------------------------------------------------------------------------------------------------------	
//	@Override
//	public APIResponse<UserBean> findByEmailId(UserBean userBean, HttpServletRequest request) {
//		Optional<User> userOptional = userRepository.findByUsername(userBean.getUsername());
//		if (userOptional.isPresent()) {
//			User user = userOptional.get();
//			String token = UUID.randomUUID().toString();
//			createPasswordResetTokenForUser(userBean, token);
//			mailSender.send(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));
//			return APIResponse.<UserBean>builder().status(Constants.SUCCESS.getValue())
//					.response(modelMapper.map(user, UserBean.class)).message(Messages.MAIL_SENT_SUCCESSFULLY).build();
//		}
//		return APIResponse.<UserBean>builder().status(Constants.FAILED.getValue()).message(Messages.DATA_NOT_FOUND)
//				.build();
//	}

	private String getAppUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

//	private SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, User user) {
//		String url = contextPath + "/changePassword?token=" + token;
//		String message = messages.getMessage("message.resetPassword", null, locale);
//		return constructEmail("Reset Password", message + " \r\n" + url, user);
//	}

//	private SimpleMailMessage constructEmail(String subject, String body, User user) {
//		SimpleMailMessage email = new SimpleMailMessage();
//		email.setSubject(subject);
//		email.setText(body);
//		email.setTo(user.getUsername());
//		email.setFrom(supportEmail);
//		return email;
//	}

	@Override
	public APIResponse<String> changeUserPassword(User user, String password) {
		user.setPassword(SecurityUtility.passwordEncoder().encode(password));
		userRepository.save(user);
		return APIResponse.<String>builder().status(Constants.SUCCESS.getValue())
		.response(user.getUsername()).message(Messages.PASSWORD_UPDATED)
		.build();
		
	}

	@Override
	public PasswordResetToken getPasswordResetToken(final String token) {
		return passwordTokenRepository.findByToken(token);
	}

	@Override
	public void createPasswordResetTokenForUser(final User user, final String token) {
		final PasswordResetToken myToken = new PasswordResetToken(token, user);
		passwordTokenRepository.save(myToken);
	}

	@Override
	public User findUserByEmail(final String email) {
		Optional<User> optionalUser = userRepository.findByUsername(email);
		if (optionalUser.isPresent()) {
			return optionalUser.get();
		}
		return null;
	}

//
	@Override
	public User getUser(final String verificationToken) {
		final VerificationToken token = tokenRepository.findByToken(verificationToken);
		if (token != null) {
			return token.getUser();
		}
		return null;
	}

	@Override
	public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
		VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
		vToken.updateToken(UUID.randomUUID().toString());
		vToken = tokenRepository.save(vToken);
		return vToken;
	}

	@Override
	public APIResponse<UserBean> findByEmailId(UserBean userBean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User registerNewUserAccount(final UserRegistration userRegistration) {
		if (emailExists(userRegistration.getUsername())) {
			throw new UserAlreadyExistException(
					"There is an account with that email address: " + userRegistration.getUsername());
		}
		final User user = new User();

		user.setFirstName(userRegistration.getFirstName());
		user.setLastName(userRegistration.getLastName());
		user.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
		user.setUsername(userRegistration.getUsername());
		user.setUsing2FA(userRegistration.isUsing2FA());
		user.setCreateDate(new Date());
		user.setMobile(userRegistration.getMobile());
		user.setAddress(userRegistration.getAddress());
//      user.setUserRoles(Collections.singletonList(roleRepository.findByName("ROLE_USER")));
		return userRepository.save(user);
	}

	private boolean emailExists(final String email) {
		return userRepository.findByUsername(email).isPresent();
	}

}
