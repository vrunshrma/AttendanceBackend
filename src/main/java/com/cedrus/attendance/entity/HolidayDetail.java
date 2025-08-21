package com.cedrus.attendance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "HOLIDAY_DETAIL")
public class HolidayDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long holidayId;

	@Column(name = "HOLIDAY_DATE")
	private LocalDate holidayDate;

	@Column(name = "REASON", length = 255) // Adjust length as needed
	private String reason;

	public Long getHolidayId() {
		return holidayId;
	}

	public void setHolidayId(Long holidayId) {
		this.holidayId = holidayId;
	}

	public LocalDate getHolidayDate() {
		return holidayDate;
	}

	public void setHolidayDate(LocalDate holidayDate) {
		this.holidayDate = holidayDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
