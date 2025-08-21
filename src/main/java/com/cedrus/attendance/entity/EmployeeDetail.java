package com.cedrus.attendance.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity(name = "employeedetail")
public class EmployeeDetail {

	@Id
	@Column(name = "employeeId")
	String employeeId;
	
	@Column(name = "firstname")
	String firstName;
	
	@Column(name = "lastname")
	String lastName;
	
	@Column(name = "username")
	String username;
	
	@Column(name = "mobile")
	String mobile ;

	@Column(name = "dob")
	String dob;

	@Column(name = "gender")
	String gender;

	@Column(name = "address")
	String address;

	@Column(name = "department")
	String department;
	
	@Column(name = "password")
	String password;
	
	@Column(name = "confirmPassword")
	String confirmPassword;
	
	@Column(name = "role")
	String role;

	@Column(name = "mailid")
	String mailId;

	@Column(name = "dateofjoining")
	long dateOfJoining;

	@Column(name = "salary")
	String salary;

	private String shiftType;

	@OneToOne(cascade = CascadeType.ALL)
	private ShiftTime shiftTime;
	
	public ShiftTime getShiftTime() {
		return shiftTime;
	}

	public void setShiftTime(ShiftTime shiftTime) {
		this.shiftTime = shiftTime;
	}

	public String getShift() {
		return shiftType;
	}

	public void setShift(String shift) {
		this.shiftType = shift;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public long getDateOfJoining() {
		return dateOfJoining;
	}

	public void setDateOfJoining(long dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}
}
