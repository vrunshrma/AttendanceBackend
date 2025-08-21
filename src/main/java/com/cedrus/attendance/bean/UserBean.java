package com.cedrus.attendance.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.cedrus.attendance.entity.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String firstName;
	private String lastName;
	private String username;
	private String mobile;
	@JsonIgnore
	private String password;
	private String address;
	private String isActive;
	private String isDeleted;
	private Date createDate;
	private Date updateDate;
	private boolean isUsing2FA; 
	private String department;
}
