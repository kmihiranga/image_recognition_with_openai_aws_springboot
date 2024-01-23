package com.netwizsoft.chatgptimagerecognize.controller.exception;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.netwizsoft.chatgptimagerecognize.domain.error.ErrorMessage;

import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.model.S3Exception;

@ControllerAdvice
public class HttpExceptionHandlerController {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorMessage> handleNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
		var errors = methodArgumentNotValidException.getAllErrors();
		
		ErrorMessage message = null;
		
		if (!errors.isEmpty()) {
			message = new ErrorMessage(400, errors.get(0).getDefaultMessage());
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
		
		message = new ErrorMessage(400, "Bad Request");
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({NoSuchElementException.class, NumberFormatException.class})
	public ResponseEntity<ErrorMessage> handleNoSuchElementException(Exception exception) {
		ErrorMessage message = new ErrorMessage(404, "Not Found");
		return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({HttpMessageNotReadableException.class, HttpRequestMethodNotSupportedException.class})
	public ResponseEntity<ErrorMessage> handleNotReadableException(Exception exception) {
		ErrorMessage message = new ErrorMessage(400, "Bad Request");
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorMessage> handleIllegalException(IllegalArgumentException exception) {
		ErrorMessage message = new ErrorMessage(400, exception.getMessage());
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(S3Exception.class)
	public ResponseEntity<ErrorMessage> handleS3Exception(S3Exception exception) {
		int intCode = Integer.parseInt(exception.awsErrorDetails().errorCode());
		ErrorMessage message = new ErrorMessage(intCode, exception.awsErrorDetails().errorMessage());
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(SdkException.class)
	public ResponseEntity<ErrorMessage> handleAwsSdkException(SdkException exception) {
		ErrorMessage message = new ErrorMessage(400, exception.getMessage());
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}
}
