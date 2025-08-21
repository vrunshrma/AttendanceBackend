package com.cedrus.attendance.config;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        // Any initialization code, if necessary
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        // Add your custom password validation logic here
        // Example: at least 8 characters, one uppercase, one lowercase, one digit, one special character
        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecialChar = password.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch));
        boolean isAtLeast8 = password.length() >= 8;
        
        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar && isAtLeast8;
    }
}
