package com.cedrus.attendance.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.cedrus.attendance.entity.AttendanceDetail;
import com.cedrus.attendance.service.AttendanceService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/attendancedetail")
public class AttendanceController {

	@Autowired
	AttendanceService attendanceservice;
	
	

	@PostMapping(path = "/addatten")
	@CrossOrigin
	@ResponseBody
	public ResponseEntity<?> addUser(@RequestBody AttendanceDetail attendancedetail) {
		HttpHeaders headers = new HttpHeaders();
		try {
			// System.out.println("en da ipdi
			// "+employeeid+employeeservice.findByempid(employeeid));
			return ResponseEntity.status(HttpStatus.CREATED).headers(headers)
					.body(attendanceservice.addUser(attendancedetail));
		} catch (Exception e) {
			headers.add("Message", "false");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers)
					.body("Failed to add the user");
		}

	}

	@GetMapping(path = "/findbydate")
	@CrossOrigin
	@ResponseBody
	public ResponseEntity<?> findByEmployeeIdAndDate(@RequestParam String employeeId, @RequestParam String date) {
		HttpHeaders headers = new HttpHeaders();

		try {
			// System.out.println("en da ipdi
			// "+employeeid+employeeservice.findByempid(employeeid));
			return ResponseEntity.status(HttpStatus.CREATED).headers(headers)
					.body(attendanceservice.findByEmployeeIdAndDate(employeeId, date));
		} catch (Exception e) {
			headers.add("Message", "false");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers)
					.body("Failed to add the user");
		}

	}

	@GetMapping(path = "/findByYearMonthEmpId")
	@CrossOrigin
	@ResponseBody
	public ResponseEntity<?> findByEmployeeIdMonthYear(@RequestParam String employeeId, @RequestParam String month,
			@RequestParam String year) {
		HttpHeaders headers = new HttpHeaders();

		try {
			// System.out.println("en da ipdi
			// "+employeeid+employeeservice.findByempid(employeeid));
			return ResponseEntity.status(HttpStatus.CREATED).headers(headers)
					.body(attendanceservice.findByEmployeeIdAndYearMonth(employeeId, year, month));
		} catch (Exception e) {
			headers.add("Message", "false");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers)
					.body("Failed to add the user");
		}

	}

	@PostMapping(path = "/updateAttendance")
	@CrossOrigin
	@ResponseBody
	public ResponseEntity<?> updateAttendance(@RequestBody AttendanceDetail attendancedetail) {
		HttpHeaders headers = new HttpHeaders();

		try {
			// System.out.println("en da ipdi
			// "+employeeid+employeeservice.findByempid(employeeid));
			return ResponseEntity.status(HttpStatus.CREATED).headers(headers)
					.body(attendanceservice.updateAttendance(attendancedetail));
		} catch (Exception e) {
			headers.add("Message", "false");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers)
					.body("Failed to add the user");
		}

	}

//	 
//	 @PostMapping(path = "/addatten")
//	 @ResponseBody
//	 public String addUser(@RequestBody AttendanceDetail attendancedetail) {
//	        
//	        	attendanceservice.addUser(attendancedetail);
//	    	
//	 }

	@GetMapping(path = "/findAllatten")
	@CrossOrigin
	@ResponseBody
	public ResponseEntity<?> findAllUser() {
		HttpHeaders headers = new HttpHeaders();

		try {
			// System.out.println("en da ipdi
			// "+employeeid+employeeservice.findByempid(employeeid));
			return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(attendanceservice.findAllUser());
		} catch (Exception e) {
			headers.add("Message", "false");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers)
					.body("Failed to add the user");
		}

	}

	@GetMapping(path = "/findbyempidatten")
	@CrossOrigin
	@ResponseBody
	public ResponseEntity<?> findByempid(@RequestParam String employeeid) {
		HttpHeaders headers = new HttpHeaders();

		try {
			// System.out.println("en da ipdi
			// "+employeeid+employeeservice.findByempid(employeeid));
			return ResponseEntity.status(HttpStatus.CREATED).headers(headers)
					.body(attendanceservice.findByempid(employeeid));
		} catch (Exception e) {
			headers.add("Message", "false");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers)
					.body("Failed to add the user");
		}

	}

	@GetMapping(path = "/findbyAbsentRequest")
	@CrossOrigin
	@ResponseBody
	public ResponseEntity<?> findbyAbsentRequest(@RequestParam String employeeId, @RequestParam String month) {
		HttpHeaders headers = new HttpHeaders();

		try {
			// System.out.println("en da ipdi
			// "+employeeid+employeeservice.findByempid(employeeid));
			return ResponseEntity.status(HttpStatus.CREATED).headers(headers)
					.body(attendanceservice.findByAbsentRequest(employeeId, month));
		} catch (Exception e) {
			headers.add("Message", "false");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers)
					.body("Failed to add the user");
		}

	}

	@GetMapping(path = "/findEmployeeListByAbsentRequest")
	@CrossOrigin
	@ResponseBody
	public ResponseEntity<?> findEmployeeListByAbsentRequest(@RequestParam String month) {
		HttpHeaders headers = new HttpHeaders();

		try {
			// System.out.println("en da ipdi
			// "+employeeid+employeeservice.findByempid(employeeid));
			return ResponseEntity.status(HttpStatus.CREATED).headers(headers)
					.body(attendanceservice.findEmployeeListByAbsentRequest(month));
		} catch (Exception e) {
			headers.add("Message", "false");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers)
					.body("Failed to add the user");
		}

	}

	@GetMapping(path = "/updateAbsentStatus")
	@CrossOrigin
	@ResponseBody
	public ResponseEntity<?> UpdateAbsentStatus(@RequestParam String employeeID, @RequestParam String date,
			@RequestParam String status) {
		HttpHeaders headers = new HttpHeaders();

		try {
			// System.out.println("en da ipdi
			// "+employeeid+employeeservice.findByempid(employeeid));
			return ResponseEntity.status(HttpStatus.CREATED).headers(headers)
					.body(attendanceservice.updateAbsentStatus(employeeID, date, status));
		} catch (Exception e) {
			headers.add("Message", "false");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers)
					.body("Failed to add the user");
		}

	}

//	 @GetMapping(path = "/findbyDandmanda")
//	 @CrossOrigin
//	    @ResponseBody
//	    public ResponseEntity<?> countByDepartmentIdAndMonthAndAvailable(@RequestParam String employeeid,@RequestParam String departmentid,@RequestParam String month,@RequestParam Boolean available) {
//		 HttpHeaders headers = new HttpHeaders();
//
//	        try {
//	    		return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body( attendanceservice.countByDepartmentIdAndMonthAndAvailable(departmentid,month,available));
//	    }catch (Exception e) {
//	    	headers.add("Message", "false");
//	    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body("Failed to add the user");
//		}
//	  	}

	@GetMapping(path = "/findbyDandm")
	@CrossOrigin
	@ResponseBody
	public ResponseEntity<?> countByDepartmentIdAndMonth(@RequestParam String departmentid,
			@RequestParam String month) {
		HttpHeaders headers = new HttpHeaders();

		try {
			return ResponseEntity.status(HttpStatus.CREATED).headers(headers)
					.body(attendanceservice.countByDepartmentIdAndMonth(departmentid, month));
		} catch (Exception e) {
			headers.add("Message", "false");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers)
					.body("Failed to add the user");
		}
	}

	@GetMapping(path = "/findbyEandDandm")
	@CrossOrigin
	@ResponseBody
	public ResponseEntity<?> countByEmployeeIdAndDepartmentIdAndMonth(@RequestParam String employeeid,
			@RequestParam String departmentid, @RequestParam String date) {
		HttpHeaders headers = new HttpHeaders();

		try {
			return ResponseEntity.status(HttpStatus.CREATED).headers(headers)
					.body(attendanceservice.countByEmployeeIdAndDepartmentIdAndMonth(employeeid, departmentid, date));
		} catch (Exception e) {
			headers.add("Message", "false");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers)
					.body("Failed to add the user");
		}
	}

	@GetMapping(path = "/findbyEandD")
	@CrossOrigin
	@ResponseBody
	public ResponseEntity<?> countByEmployeeIdAndDepartmentId(@RequestParam String employeeid,
			@RequestParam String departmentid) {
		HttpHeaders headers = new HttpHeaders();

		try {
			return ResponseEntity.status(HttpStatus.CREATED).headers(headers)
					.body(attendanceservice.countByEmployeeIdAndDepartmentId(employeeid, departmentid));
		} catch (Exception e) {
			headers.add("Message", "false");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers)
					.body("Failed to add the user");
		}
	}

//	 @GetMapping(path = "/findbyEandDandA")
//	 @CrossOrigin
//	    @ResponseBody
//	    public ResponseEntity<?> countByEmployeeIdAndDepartmentIdAndAvailable(@RequestParam String employeeid,@RequestParam String departmentid,@RequestParam Boolean available) {
//		 HttpHeaders headers = new HttpHeaders();
//	        try {   	
//	    		return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body( attendanceservice.countByEmployeeIdAndDepartmentIdAndAvailable(employeeid,departmentid,available));
//	    }catch (Exception e) {
//	    	headers.add("Message", "false");
//	    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body("Failed to add the user");
//		}
//		  	
//		  
//	  	}

	@GetMapping(path = "/findbydepidatten")
	@CrossOrigin
	@ResponseBody
	public ResponseEntity<?> findBydeptid(@RequestParam String departmentid) {
		HttpHeaders headers = new HttpHeaders();

		try {
			// System.out.println("en da ipdi
			// "+employeeid+employeeservice.findByempid(employeeid));
			return ResponseEntity.status(HttpStatus.CREATED).headers(headers)
					.body(attendanceservice.findBydeptid(departmentid));
		} catch (Exception e) {
			headers.add("Message", "false");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers)
					.body("Failed to add the user");
		}

	}

//	 @GetMapping(path = "/sortview")
//	 @CrossOrigin
//	    @ResponseBody
//	    public ResponseEntity<?> sortdeptview(@RequestParam String departmentId,@RequestParam String month,@RequestParam String shift) {
//		 HttpHeaders headers = new HttpHeaders();
//
//	        try {
//	        	//System.out.println("en da ipdi "+employeeid+employeeservice.findByempid(employeeid));
//	    		return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(attendanceservice.sortdeptview(departmentId,month,shift));
//	    }catch (Exception e) {
//	    	headers.add("Message", "false");
//	    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body("Failed to add the user");
//		}
//
//	  	}

	@GetMapping(path = "/findbybothidatten")
	@CrossOrigin
	@ResponseBody
	public AttendanceDetail findByName(@RequestParam String employeeid, @RequestParam String departmentid) {
		return attendanceservice.findByName(employeeid, departmentid);
	}

	@GetMapping(path = "/updateatten")
	@CrossOrigin
	@ResponseBody
	public ResponseEntity<?> updateUser(@RequestParam String employeeid, @RequestParam String attendanceStatus,
			@RequestParam LocalDate date) {
		HttpHeaders headers = new HttpHeaders();

		try {
			return ResponseEntity.status(HttpStatus.CREATED).headers(headers)
					.body(attendanceservice.updateUser(employeeid, attendanceStatus, date));
		} catch (Exception e) {
			headers.add("Message", "false");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers)
					.body("Failed to add the user");
		}

	}

	@GetMapping(path = "/deletebyempidatten")
	@CrossOrigin
	@ResponseBody
	public String deleteByEmpid(@RequestParam String employeeId, @RequestParam String year, @RequestParam String month,
			@RequestParam String date) {
		return attendanceservice.deleteByEmpAttDetails(employeeId, year, month, date);
	}

	@GetMapping(path = "/deletebydepidatten")
	@CrossOrigin
	@ResponseBody
	public ResponseEntity<?> deleteBydeptid(@RequestParam String departmentid) {
		HttpHeaders headers = new HttpHeaders();

		try {
			// System.out.println("en da ipdi
			// "+employeeid+employeeservice.findByempid(employeeid));
			return ResponseEntity.status(HttpStatus.CREATED).headers(headers)
					.body(attendanceservice.deleteBydeptid(departmentid));
		} catch (Exception e) {
			headers.add("Message", "false");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers)
					.body("Failed to add the user");
		}

	}

	@GetMapping(path = "/deletebybothidatten")
	@CrossOrigin
	@ResponseBody
	public String deleteByUserNameAndPassword(@RequestParam String employeeid, @RequestParam String departmentid) {
		return attendanceservice.deleteByUserNameAndPassword(employeeid, departmentid);
	}

}