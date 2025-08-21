package com.cedrus.attendance.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "LEAVE_DETAIL")
public class LeaveDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "EMPLOYEE_ID", length = 20) // Assuming a maximum length of 20 characters for employee ID
	String employeeId;

	@Column(name = "LEAVE_REASON", length = 255) // Adjust length as needed
	String leaveReason;

	@Column(name = "DEPARTMENT_ID", length = 20) // Assuming a maximum length of 20 characters for department ID
	String departmentId;

	@Column(name = "LEAVE_DATE")
	LocalDate leaveDate;

	@Column(name = "FROM_DATE")
	LocalDate fromDate;

	@Column(name = "TO_DATE")
	LocalDate toDate;

	@Column(name = "LEAVE_TYPE", length = 50) // Adjust length as needed
	String leaveType;

	@Column(name = "TYPE", length = 50) // Adjust length as needed
	String type;

}
