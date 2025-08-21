package com.cedrus.attendance.repository;



import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cedrus.attendance.entity.LeaveDetail;


@Repository
public interface LeaveRepository extends CrudRepository<LeaveDetail, Long> {

   
    LeaveDetail[] findByEmployeeId(String employeeId);


    LeaveDetail findByEmployeeIdAndFromDate(String employeeId, String fromDate);

    
    LeaveDetail[] findBydepartmentId(String deptId);
    
    
    @Transactional
    void deleteByEmployeeId(String employeeId);

}	