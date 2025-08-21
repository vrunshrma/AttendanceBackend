package com.cedrus.attendance.repository;



import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cedrus.attendance.entity.EmployeeDetail;
@Repository
public interface EmployeeRepository extends CrudRepository<EmployeeDetail, String> {
   
    EmployeeDetail findByEmployeeId(String employeeid);
    EmployeeDetail findByEmployeeIdAndShiftType(String employeeid,String shift);

    EmployeeDetail[] findByDepartment(String department);
    @Transactional
    void deleteByEmployeeId(String employeeId);

}	