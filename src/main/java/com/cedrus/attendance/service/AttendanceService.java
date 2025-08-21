package com.cedrus.attendance.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cedrus.attendance.entity.AttendanceDetail;
import com.cedrus.attendance.entity.User;
import com.cedrus.attendance.repository.AttendanceRepository;
import com.cedrus.attendance.repository.UserRepository;
import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import lombok.extern.slf4j.Slf4j;

@Service("attendanceservice")
@Slf4j
public class AttendanceService {

	private AttendanceRepository attendancerepository;

	private UserRepository userRepository;

//	@Value("${twilio.accountSid}")
//    private String accountSid;
//
//    @Value("${twilio.authToken}")
//    private String authToken;
//
//    @Value("${twilio.phoneNumber}")
//    private String phoneNumber;


	@Autowired
	public AttendanceService(AttendanceRepository attendancerepository, UserRepository userRepository) {
		super();
		this.attendancerepository = attendancerepository;
		this.userRepository = userRepository;
	}

//	public void SendWhatsApp() {
//		Twilio.init(accountSid, authToken);
//		Message message = Message.creator(new com.twilio.type.PhoneNumber("whatsapp:+919717198898"),
//				new com.twilio.type.PhoneNumber("whatsapp:+14155238886"), " ").create();
//
//		System.out.println(message.getSid());
//	}
//
//	public void receiveWhatsappMessage() {
//		Twilio.init(accountSid, authToken);
//		Message message = Message.creator(new com.twilio.type.PhoneNumber("whatsapp:+919717198898"),
//				new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
//				"Your appointment is coming up on July 21 at 3PM").create();
//
//		System.out.println(message.getSid());
//	}

	@Scheduled(cron = "0 0 12 * * ?") // Runs every minute
	public void markAttendanceForToday() {
		
		try {
		    System.out.println("Marking attendance for today at 12:00 PM");

		    LocalDate today = LocalDate.now();
		    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		    String formattedDate = today.format(formatter);
		    LocalDate date = LocalDate.parse(formattedDate, formatter);
		    int month = date.getMonthValue();
		    int year = date.getYear();
		    
		    log.info("Checking attendance for date: " + formattedDate);

		    // Fetch attendance records
		    List<AttendanceDetail> attendanceList = attendancerepository.findByAttendanceDate(formattedDate);

		    // Retrieve all users
		    List<User> usersList = userRepository.findAll();

		    if (attendanceList.isEmpty()) {
		        log.info("No attendance records found. Marking attendance...");
		        
		        // Mark attendance for each user
		        for (User user : usersList) {
		            AttendanceDetail newAttendance = new AttendanceDetail();
		            newAttendance.setAttendanceDate(formattedDate);
		            newAttendance.setAttendanceStatus("Absent");
		            newAttendance.setStatus("Approved");
		            newAttendance.setEmployeeId(String.valueOf(user.getId()));
		            newAttendance.setMonth(String.valueOf(month));
		            newAttendance.setYear(String.valueOf(year));

		            attendancerepository.save(newAttendance);
		            log.info("Marked attendance for user: " + user.getId());
		        }
		    } else {
		        log.info("Attendance records exist. Checking for missing users...");

		        Set<String> markedUserIds = attendanceList.stream()
		                .map(AttendanceDetail::getEmployeeId)
		                .collect(Collectors.toSet());

		        for (User user : usersList) {
		            String userId = String.valueOf(user.getId());

		            if (!markedUserIds.contains(userId)) {
		                AttendanceDetail newAttendance = new AttendanceDetail();
		                newAttendance.setAttendanceDate(formattedDate);
		                newAttendance.setAttendanceStatus("Absent");
		                newAttendance.setStatus("Approved");
		                newAttendance.setEmployeeId(userId);
		                newAttendance.setMonth(String.valueOf(month));
		                newAttendance.setYear(String.valueOf(year));

		                attendancerepository.save(newAttendance);
		                log.info("Marked attendance as Absent for user: " + userId);
		            }
		        }
		    }
		    
		}catch(Exception e){
	    	System.out.println(e.getMessage());
	    }

	}



	public AttendanceDetail addUser(AttendanceDetail attendancedetail) {

		attendancerepository.save(attendancedetail);

//		SendWhatsApp();

		return attendancedetail;

	}

	public List<AttendanceDetail> findAllUser() {

		return (List<AttendanceDetail>) attendancerepository.findAll();
	}

	public AttendanceDetail[] findByempid(String employeeid) {
		AttendanceDetail[] attendanceList = attendancerepository.findByEmployeeId(employeeid);

		if (attendanceList != null) {
			return attendanceList;
		}
		return null;
	}

	public long countByEmployeeIdAndDepartmentId(String employeeid, String departmentid) {
		long attendanceList = attendancerepository.countByEmployeeIdAndDepartmentId(employeeid, departmentid);
		return attendanceList;

	}

	public String updateAttendance(AttendanceDetail attendanceDetail) {
		AttendanceDetail attendanceDetails = findByEmployeeIdAndDate(attendanceDetail.getEmployeeId(),
				attendanceDetail.getAttendanceDate());
//		AttendanceDetail attendanceList = (AttendanceDetail) attendancerepository
//				.findByEmployeeIdAndAttendanceDate(employeeid, date);
		if (attendanceDetails != null) {
			attendanceDetails.setAttendanceStatus(attendanceDetail.getAttendanceStatus());
			attendanceDetails.setCheckIn(attendanceDetail.getCheckIn());
			attendanceDetails.setCheckOut(attendanceDetail.getCheckOut());
			attendancerepository.save(attendanceDetail);
			return "User updated Successfully";
		}
		return "User update Failed";
	}

	public List<AttendanceDetail> findEmployeeListByAbsentRequest(String month) {
		List<AttendanceDetail> attendanceDetail = attendancerepository.findByMonthAndStatus(month, "Pending");
		return attendanceDetail;

	}

	public List<AttendanceDetail> findByAbsentRequest(String employeeid, String month) {
		return attendancerepository.findByEmployeeIdAndMonthAndStatus(employeeid, month, "Pending");

	}

//	public long countByDepartmentIdAndMonthAndAvailable(String departmentid, String month, Boolean available) {
//		long attendanceList = attendancerepository.countByDepartmentIdAndMonthAndAvailable(departmentid, month,
//				available);
//		return attendanceList;
//
//	}

	public long countByDepartmentIdAndMonth(String departmentid, String month) {
		long attendanceList = attendancerepository.countByDepartmentIdAndMonth(departmentid, month);
		return attendanceList;

	}

	public long countByEmployeeIdAndDepartmentIdAndMonth(String employeeid, String departmentid, String month) {
		long attendanceList = attendancerepository.countByEmployeeIdAndDepartmentIdAndMonth(employeeid, departmentid,
				month);
		return attendanceList;

	}

	public AttendanceDetail updateAbsentStatus(String employeeid, String date, String status) {

		// int value =
		// attendancerepository.updateAttendanceStatusByEmployeeIDAndDate(employeeid,
		// status, date);
		AttendanceDetail attendanceDetail = attendancerepository.findAttendanceDetailByEmployeeIdAndDate(employeeid,
				date);
		attendanceDetail.setStatus(status);
		attendancerepository.save(attendanceDetail);
		log.info("attendanceDetail : " + attendanceDetail);
		return attendanceDetail;

	}

//	public long countByEmployeeIdAndDepartmentIdAndAvailable(String employeeid, String departmentid,
//			Boolean available) {
//		return attendancerepository.countByEmployeeIdAndDepartmentIdAndAvailable(employeeid, departmentid, available);
//	}

	public AttendanceDetail findByEmployeeIdAndDate(String employeeId, String date) {
		AttendanceDetail attendanceList = (AttendanceDetail) attendancerepository
				.findByAttendanceDateAndEmployeeId(date, employeeId);

		if (attendanceList != null) {
			return attendanceList;
		}
		return null;
	}

	public List<AttendanceDetail> findByEmployeeIdAndYearMonth(String employeeId, String year, String month) {
		List<AttendanceDetail> attendanceList = attendancerepository.findByMonthAndYearAndEmployeeId(month, year,
				employeeId);
		int count = 0;
		if (attendanceList != null) {
			for (AttendanceDetail attendance : attendanceList) {
				count++;
				log.info("Attendance count : " + count + " " + attendance);
			}
			return attendanceList;
		}
		return null;
	}

	public AttendanceDetail[] findBydeptid(String departmentid) {
		return (AttendanceDetail[]) attendancerepository.findByDepartmentId(departmentid);
	}

	public AttendanceDetail findByName(String employeeid, String departmentid) {
		return (AttendanceDetail) attendancerepository.findByEmployeeIdAndDepartmentId(employeeid, departmentid);
	}

//	    public Object[] sortdeptview(String departmentid,String month,String shift) {
//						AttendanceDetail[] attendanceList = (AttendanceDetail[]) 
//								attendancerepository.findByDepartmentId(departmentid);
//		
//			for(AttendanceDetail atten : attendanceList) {
//				atten.setAttencount(countByDepartmentIdAndMonth(departmentid,month));
//			}
//				return attendancerepository.findByAttendanceCount(departmentid,month,shift);
//
//	    }

	public String updateUser(String employeeid, String attendanceStatus, LocalDate date) {
		AttendanceDetail attendanceList = (AttendanceDetail) attendancerepository
				.findByEmployeeIdAndAttendanceDate(employeeid, date);
		if (attendanceList != null) {

			attendanceList.setAttendanceStatus(attendanceStatus);
			attendancerepository.save(attendanceList);
			return "User updated Successfully";
		}
		return "User update Failed";
	}

	public String deleteByEmpAttDetails(String employeeid, String year, String month, String date) {
		attendancerepository.deleteByEmployeeIdAndYearAndMonthAndAttendanceDate(employeeid, year, month, date);
		return "Record deleted successfully";
	}

	public String deleteBydeptid(String departmentid) {
		attendancerepository.deleteByDepartmentId(departmentid);
		return "Record deleted successfully";
	}

	public String deleteByUserNameAndPassword(String employeeid, String departmentid) {
		attendancerepository.deleteByEmployeeIdAndDepartmentId(employeeid, departmentid);
		return "Record deleted successfully";
	}
}
