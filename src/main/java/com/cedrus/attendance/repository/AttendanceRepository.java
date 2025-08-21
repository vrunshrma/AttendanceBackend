package com.cedrus.attendance.repository;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cedrus.attendance.entity.AttendanceDetail;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends CrudRepository<AttendanceDetail, Long> {

	AttendanceDetail[] findByEmployeeId(String employeeid);
	
	 
	AttendanceDetail[] findByDepartmentId(String departmentid);
	
	AttendanceDetail findByAttendanceDateAndEmployeeId(String date, String employeeId);
	
	AttendanceDetail findByEmployeeIdAndDepartmentId(String employeeid,String departmentid);
	
	Long countByEmployeeIdAndDepartmentId(String employeeid,String departmentid);
	
	//Long countByEmployeeIdAndDepartmentIdAndAvailable(String employeeid,String departmentid,Boolean available);
	
	Long countByEmployeeIdAndDepartmentIdAndMonth(String employeeid,String departmentid,String month);
	
	List<AttendanceDetail> findByMonthAndYearAndEmployeeId(String month, String year, String employeeId);

	@Query("SELECT a FROM AttendanceDetail a WHERE a.departmentId = ?1 AND a.month = ?2 GROUP BY a.employeeId")
	Long countByDepartmentIdAndMonth(String departmentid,String month);
	
	@Query("SELECT a FROM AttendanceDetail a WHERE a.employeeId = :employeeId AND a.attendanceDate = :date")
	AttendanceDetail findAttendanceDetailByEmployeeIdAndDate(String employeeId, String date);
	
	List<AttendanceDetail>findByAttendanceDate(String date);
	
	List<AttendanceDetail> findByEmployeeIdAndMonthAndStatus(String employeeid, String month, String status);
	
	List<AttendanceDetail> findByMonthAndStatus(String month, String status);

	@Transactional
	void deleteByEmployeeIdAndDepartmentId(String employeeid,String departmentid);
	
	@Transactional
	void deleteByDepartmentId(String departmentid);
	
	@Transactional
	void deleteByEmployeeIdAndYearAndMonthAndAttendanceDate(String employeeid, String year, String month, String date);

	AttendanceDetail findByEmployeeIdAndAttendanceDate(String employeeid, LocalDate date);

//	@Query(value = "SELECT employeeId,count(attendanceStatus) as total FROM AttendanceDetail where attendanceStatus=1 and department_id = ?1 and month = ?2 and shift = ?3 group by employee_id order by count(attendanceStatus) desc")
//	Object[] findByAttendanceCount(String departmentid,String month,String shift);

}	