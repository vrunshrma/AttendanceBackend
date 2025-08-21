package com.cedrus.attendance.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cedrus.attendance.entity.DepartmentDetail;

@Repository
public interface DepartmentRepository extends CrudRepository<DepartmentDetail, String> {

	
   DepartmentDetail findByDepartmentId(String departmentid);
   
   @Transactional
   void deleteByDepartmentId(String departmentId);	

}	