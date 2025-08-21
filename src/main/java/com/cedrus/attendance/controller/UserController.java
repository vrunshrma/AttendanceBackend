package com.cedrus.attendance.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cedrus.attendance.bean.UserBean;
import com.cedrus.attendance.bean.UserRegistration;
import com.cedrus.attendance.common.BaseUrls;
import com.cedrus.attendance.common.Constants;
import com.cedrus.attendance.entity.RoleType;
import com.cedrus.attendance.entity.User;
import com.cedrus.attendance.response.APIResponse;
import com.cedrus.attendance.service.IUserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(BaseUrls.USER_BASE_URL)
public class UserController {

	private IUserService iUserService;

	@GetMapping(BaseUrls.GET_ALL_EMPLOYEES)
	public ResponseEntity<APIResponse<List<UserBean>>> findAllUser() {
		return ResponseEntity.status(HttpStatus.OK).body(iUserService.findAllUser());
	}

	@PostMapping(path = "/forgotPassword")
	public ResponseEntity<APIResponse<UserBean>> findByEmailId(@RequestBody UserBean emailId) {
		return ResponseEntity.status(HttpStatus.OK).body(iUserService.findByEmailId(emailId));
	}

	@PostMapping("/savePassword")
	public ResponseEntity<APIResponse<String>> savePassword(@RequestParam("password") final String password) {
		final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.status(HttpStatus.OK).body(iUserService.changeUserPassword(user, password));
	}

	@GetMapping(path = "/findbyempid")
	public ResponseEntity<APIResponse<UserBean>> findByEmployeeId(@RequestParam String employeeId) {
		return ResponseEntity.status(HttpStatus.OK).body(iUserService.findByEmployeeId(employeeId));
	}
	
	@GetMapping(path = "/findbyEmployeeIds")
	@CrossOrigin
	@ResponseBody
	public ResponseEntity<?> findListOfUsersByEmployeeIds(@RequestParam String employeeIds) {
		HttpHeaders headers = new HttpHeaders();

		try {
			// System.out.println("en da ipdi
			// "+employeeid+employeeservice.findByempid(employeeid))
			List<String> employeeIdList = Arrays.asList(employeeIds.split(","));
			return ResponseEntity.status(HttpStatus.CREATED).headers(headers)
					.body(iUserService.findUsersByEmployeeIds(employeeIdList));
		} catch (Exception e) {
			headers.add("Message", "false");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers)
					.body("Failed to add the user");
		}

	}
	
	@PostMapping(BaseUrls.RESET_PASSWORD)
	//@PreAuthorize("hasRole('PASSWORD_RESET')")
	public ResponseEntity<APIResponse<Object>> resetPassword(@RequestBody UserRegistration userRegistration) {
		APIResponse<Object> response = iUserService.updatePassword(userRegistration);
		if (StringUtils.equals(response.getStatus(), Constants.SUCCESS.getValue()))
			return ResponseEntity.ok(response);
		return ResponseEntity.badRequest().body(response);
	}
	
	@PostMapping(BaseUrls.USER_CREATE)
	public ResponseEntity<APIResponse<Object>> createUser(@Valid @RequestBody UserRegistration userRegistration) {
		log.info("Inside UserController user registration start...");
		APIResponse<Object> response = iUserService.createUser(userRegistration);
		if (StringUtils.equals(response.getStatus(), Constants.SUCCESS.getValue()))
			return ResponseEntity.ok(response);
		return ResponseEntity.badRequest().body(response); 
	}

	@PostMapping(path = "/updateuser")
	public ResponseEntity<APIResponse<String>> updateUser(@RequestBody UserBean useraccount) {
		return ResponseEntity.status(HttpStatus.OK).body(iUserService.updateUser(useraccount));
	}

	private boolean hasRole(RoleType roleType) {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.anyMatch((authority -> authority.getAuthority().equals(roleType.toString())));
	}

}
