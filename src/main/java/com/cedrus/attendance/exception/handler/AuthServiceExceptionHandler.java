package com.cedrus.attendance.exception.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cedrus.attendance.common.Constants;
import com.cedrus.attendance.exceptions.ExceptionMessages;
import com.cedrus.attendance.exceptions.ExceptionResponse;
import com.cedrus.attendance.exceptions.InValidDataException;
import com.cedrus.attendance.exceptions.UserIsNotFoundException;
import com.cedrus.attendance.exceptions.UserNotAutharizedException;
import com.cedrus.attendance.response.APIResponse;
import com.cedrus.attendance.response.ErrorResponse;

@RestControllerAdvice
public class AuthServiceExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<APIResponse<ErrorResponse>> handleMethodArgumentException(
			MethodArgumentNotValidException exception) {
		List<ErrorResponse> errors = new ArrayList<>();
		exception.getBindingResult().getFieldErrors().forEach(error -> {
			ErrorResponse errorResponse = new ErrorResponse(error.getField(), error.getDefaultMessage());
			errors.add(errorResponse);
		});
		return new ResponseEntity<>(
				APIResponse.<ErrorResponse>builder().status(Constants.FAILED.getValue())
						.message(ExceptionMessages.VALIDATION_FAILED.getValue()).errors(errors).build(),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserNotAutharizedException.class)
	public ResponseEntity<ExceptionResponse<Object>> handleUnAutherizedException(UserNotAutharizedException exception) {
		return new ResponseEntity<>(
				ExceptionResponse.builder().status(Constants.FAILED.getValue()).message(exception.getMessage()).build(),
				HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(UserIsNotFoundException.class)
	public ResponseEntity<ExceptionResponse<Object>> handleUserNotActiveException(UserIsNotFoundException exception) {
		return new ResponseEntity<>(
				ExceptionResponse.builder().status(Constants.FAILED.getValue()).message(exception.getMessage()).build(),
				HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(FileUploadException.class)
	public ResponseEntity<ExceptionResponse<Object>> handleFileUploadException(FileUploadException exception) {
		return new ResponseEntity<>(
				ExceptionResponse.builder().status(Constants.FAILED.getValue()).message(exception.getMessage()).build(),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InValidDataException.class)
	public ResponseEntity<ExceptionResponse<Object>> handleInValidDataException(InValidDataException exception) {
		return new ResponseEntity<>(
				ExceptionResponse.builder().status(Constants.FAILED.getValue()).message(exception.getMessage()).build(),
				HttpStatus.BAD_REQUEST);
	}
}
