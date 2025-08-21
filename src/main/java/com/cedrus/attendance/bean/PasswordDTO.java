package com.cedrus.attendance.bean;

import com.cedrus.attendance.config.ValidPassword;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDTO {
	private String oldPassword;

    private  String token;

    @ValidPassword
    private String newPassword;
}
