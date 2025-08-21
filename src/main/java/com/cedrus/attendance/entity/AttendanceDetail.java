package com.cedrus.attendance.entity;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "ATTENDANCE_DETAIL")
public class AttendanceDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long attendanceId;

	private String employeeId;

	private String attendanceDate;

	private String departmentId;

	private String attendanceStatus;

	private String checkIn;

	private String checkOut;

	private long attendanceCount;

	private String shift;

	private String month;
	
	private String year;
	
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Long getAttendanceId() {
		return attendanceId;
	}

	public void setAttendanceId(Long attendanceId) {
		this.attendanceId = attendanceId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getAttendanceDate() {
		return attendanceDate;
	}

	public void setAttendanceDate(String attendanceDate) {
		this.attendanceDate = attendanceDate;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getAttendanceStatus() {
		return attendanceStatus;
	}

	public void setAttendanceStatus(String attendanceStatus) {
		this.attendanceStatus = attendanceStatus;
	}

	public String getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(String checkIn) {
		this.checkIn = checkIn;
	}

	public String getCheckOut() {
		return checkOut;
	}

	public void setCheckOut(String checkOut) {
		this.checkOut = checkOut;
	}

	public long getAttendanceCount() {
		return attendanceCount;
	}

	public void setAttendanceCount(long attendanceCount) {
		this.attendanceCount = attendanceCount;
	}

	public String getShift() {
		return shift;
	}

	public void setShift(String shift) {
		this.shift = shift;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	@Override
	public String toString() {
		return "AttendanceDetail [attendanceId=" + attendanceId + ", employeeId=" + employeeId + ", attendanceDate="
				+ attendanceDate + ", departmentId=" + departmentId + ", attendanceStatus=" + attendanceStatus
				+ ", checkIn=" + checkIn + ", checkOut=" + checkOut + ", attendanceCount=" + attendanceCount
				+ ", shift=" + shift + ", month=" + month + ", year=" + year + ", status=" + status + "]";
	}
	
	
	
}
