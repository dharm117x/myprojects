package com.project.handler;

import java.util.HashMap;
import java.util.Map;

import javax.security.sasl.AuthenticationException;

import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.project.dto.response.ErrorResponse;
import com.project.exception.BusinessException;
import com.project.exception.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private final static Logger log = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
    @ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
        		.contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.of(
                        "RESOURCE_NOT_FOUND",
                        ex.getMessage(),
                        request.getRequestURI(),
                        getTraceId(),
                        null
                ));
    }

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
			HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(e -> fieldErrors.put(
                        e.getField(), e.getDefaultMessage()));

        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(
                        "VALIDATION_ERROR",
                        "Invalid request data",
                        request.getRequestURI(),
                        getTraceId(),
                        fieldErrors
                ));
    }


    @ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest request) {

    	return ResponseEntity.badRequest()
                .body(ErrorResponse.of(
                        "BUSINESS_ERROR",
                        ex.getMessage(),
                        request.getRequestURI(),
                        getTraceId(),
                        null
                ));
    }
    
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleAuth(AuthenticationException ex, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of(
                        "UNAUTHORIZED",
                        "Authentication required",
                        request.getRequestURI(),
                        getTraceId(),
                        null
                ));
    }

    @ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleUnknown(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error, traceId={}", getTraceId(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(
                        "INTERNAL_ERROR",
                        "Something went wrong. Please contact support.",
                        request.getRequestURI(),
                        getTraceId(),
                        null
                ));
    }

    
	private String getTraceId() {
		return MDC.get("traceId");
	}

}
