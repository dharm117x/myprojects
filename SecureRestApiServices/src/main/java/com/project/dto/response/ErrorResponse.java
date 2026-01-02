package com.project.dto.response;

import java.time.Instant;
import java.util.Map;

public class ErrorResponse {

    private String code;        // BUSINESS_ERROR, VALIDATION_ERROR
    private String message;     // Safe client message
    private String path;        // /api/v1/users/1
    private Instant timestamp;
    private String traceId;     // For log correlation
    private Map<String, String> errors; // Field errors (optional)

    public static ErrorResponse of(
            String code,
            String message,
            String path,
            String traceId,
            Map<String, String> errors) {

        ErrorResponse r = new ErrorResponse();
        r.code = code;
        r.message = message;
        r.path = path;
        r.timestamp = Instant.now();
        r.traceId = traceId;
        r.errors = errors;
        return r;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}
    
    
}
