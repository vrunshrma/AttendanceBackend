package com.cedrus.attendance.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseUrls {

	public static final String AUTH_BASE_URL = "/api/v1/auth";
	public static final String USER_BASE_URL = "/api/v1/user";
	public static final String CUSTOMER_BASE_URL = "/api/v1/customer";
	public static final String RESET_PASSWORD = "/resetPassword";
	public static final String EMAIL_SENDER = "/mailSender";
	public static final String IS_LOGGED_IN_USER = "/isLoggedIn";
	public static final String REQUEST_PARAM_USERNAME = "/{username}";
	public static final String CHECK_USERNAME = "/{findByUsername}";
	public static final String LOGIN = "/login";
	public static final String USER_CREATE = "/create";
//	public static final String CUSTOMER_CREATE = "/create";
	public static final String GET_CUSTOMERS = "/customers";
	public static final String GET_ALL_EMPLOYEES = "/employees";
	public static final String CHANGE_PASSWORD = "/changePassword";
	public static final String SAVE_PASSWORD = "/savePassword";
	public static final String BOOKING_CREATE = "/{customerId}/booking/create";
	public static final String GET_CUSTOMER_BOOKINGS = "/{customerId}/bookings";
	public static final String CUSTOMER_SEARCH = "/{mobile}/search/customer";

}
