package com.cedrus.attendance.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.cedrus.attendance.bean.UserBean;
import com.cedrus.attendance.bean.UserRegistration;
import com.cedrus.attendance.response.APIResponse;
import com.cedrus.attendance.entity.PasswordResetToken;
import com.cedrus.attendance.entity.User;
import com.cedrus.attendance.entity.VerificationToken;

public interface IUserService {
	
	User getUser(String verificationToken);

	APIResponse<Object> createUser(UserRegistration userRegistration);
	
	List<User> findUsersByEmployeeIds(List<String> employeeIDs);

	APIResponse<List<UserBean>> findAllUser();

	APIResponse<UserBean> findByEmployeeId(String employeeId);

	APIResponse<String> updateUser(UserBean useraccount);
	
	APIResponse<UserBean> findByEmailId(UserBean userBean);
	
	APIResponse<Object> updatePassword(UserRegistration user);

	APIResponse<String> changeUserPassword(User user, String newPassword);

	PasswordResetToken getPasswordResetToken(String token);
	
	void createPasswordResetTokenForUser(User user, String token);
	
	User findUserByEmail(String email);

	VerificationToken generateNewVerificationToken(String existingToken);

	User registerNewUserAccount(@Valid UserRegistration userRegistration);

}
