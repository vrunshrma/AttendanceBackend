package com.cedrus.attendance.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticateUser implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String firstName;
	private String lastName;
	@JsonProperty("email")
	private String username;
	private String mobile;
	private String address;
	private String role;
	private String isActive;
	private String isDeleted;
	private Date createDate;
	private Date updateDate;

}
