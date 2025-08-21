package com.cedrus.attendance.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.cedrus.attendance.entity.PasswordResetToken;
import com.cedrus.attendance.entity.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{
	PasswordResetToken findByToken(String token);
	
//	@Query(value = "SELECT u* FROM user_details u JOIN PasswordResetToken p ON p.user_id = u.id WHERE p.token = :token")
//	Optional<User> getUserByPasswordResetToken(@Param("token") String token);
}
