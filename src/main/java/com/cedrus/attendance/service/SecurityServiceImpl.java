package com.cedrus.attendance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cedrus.attendance.entity.PasswordResetToken;
import com.cedrus.attendance.repository.PasswordResetTokenRepository;

@Service
public class SecurityServiceImpl implements SecurityService{

	 @Autowired
	 private PasswordResetTokenRepository tokenRepository; // Assuming you have a repository to handle tokens

//	    @Override
//	    public String validatePasswordResetToken(String token) {
//	        PasswordResetToken passToken = tokenRepository.findByToken(token);
//
//	        if (passToken == null) {
//	            return "invalidToken";
//	        }
//
//	        if (passToken.isExpired()) {
//	            return "expired";
//	        }
//
//	        return null;
//	    }


}
