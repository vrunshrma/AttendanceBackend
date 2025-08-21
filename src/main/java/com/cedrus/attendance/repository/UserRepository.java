package com.cedrus.attendance.repository;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cedrus.attendance.bean.UserBean;
import com.cedrus.attendance.entity.AttendanceDetail;
import com.cedrus.attendance.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	@Query("select u from User u where u.username = ?1 or u.mobile = ?1 and u.isActive = ?2 and u.isDeleted = ?3")
	User findByUsernameOrMobile(String param, String isActive, String isDeleted);
	
	@Query("SELECT u FROM User u WHERE u.id IN :employeeIds")
	List<User> findUsersByEmployeeIds(@Param("employeeIds") List<Long> employeeIds);

	Optional<User> findByMobile(@NotNull String mobile);

	Optional<User> findById(String employeeId);

}
