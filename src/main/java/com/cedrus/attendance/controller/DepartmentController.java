package com.cedrus.attendance.controller;

import com.cedrus.attendance.entity.DepartmentDetail;
import com.cedrus.attendance.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/departmentdetail")
public class DepartmentController {
		
		@Autowired
		DepartmentService departmentservice;
	
		@PostMapping(path="/adddepartment")
		@CrossOrigin
		@ResponseBody
		public ResponseEntity<?> adddepartment(@RequestBody DepartmentDetail departmentdetail) {
			 HttpHeaders headers = new HttpHeaders();
		        
		        try {   	
		    		return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body( departmentservice.adddepartment(departmentdetail));
		    }catch (Exception e) {
		    	headers.add("Message", "false");
		    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body("Failed to add the user");
			}
		}
		
		@GetMapping(path = "/findalldepartment")
		@CrossOrigin
		@ResponseBody
		public ResponseEntity<?> findalldepartment() {
			 HttpHeaders headers = new HttpHeaders();
		        
		        try {   	
		    		return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(departmentservice.findalldepartment());
		    }catch (Exception e) {
		    	headers.add("Message", "false");
		    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body("Failed to add the user");
			}
			  
		}
		
}
