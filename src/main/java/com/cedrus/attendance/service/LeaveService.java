package com.cedrus.attendance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cedrus.attendance.entity.LeaveDetail;
import com.cedrus.attendance.repository.LeaveRepository;

@Service("LeaveService")
public class LeaveService {

    
    private LeaveRepository LeaveRepository;
	
	@Autowired
    public LeaveService(LeaveRepository leaveRepository) {
        this.LeaveRepository = leaveRepository;   
    }

   
    public String addLeave(LeaveDetail leavedet) {
    	
    		LeaveRepository.save(leavedet);
    		return "Leave has been added, user name = " + leavedet.getEmployeeId();
    	
       
    }
    
    public List<LeaveDetail> findAllUser() {
        return (List<LeaveDetail>) LeaveRepository.findAll();
    }

    public String updateLeave(String employeeId,String fromdate,String leavetype) {
    	
    	LeaveDetail l = LeaveRepository.findByEmployeeIdAndFromDate(employeeId, fromdate);
    	if(l!=null) {
    		l.setLeaveType(leavetype);
		LeaveRepository.save(l);
		
    	}
        assert l != null;
        return "Leave has been added, user name = " + l.getEmployeeId();
	
   
}
    
    public LeaveDetail[] findByEmployeeId(String employeeId) {
        return LeaveRepository.findByEmployeeId(employeeId);
    }


    public LeaveDetail[] findByDepartmentId(String deptid) {
    	//System.out.println("HELLO"+deptid);
        return LeaveRepository.findBydepartmentId(deptid);
    }
    public String deleteByEmpid(String employeeid) {
    	LeaveRepository.deleteByEmployeeId(employeeid);
        return "Leave data has been deleted successfully.";

    }
   
    
}
