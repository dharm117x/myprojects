package com.project.dto.request;

import jakarta.validation.constraints.Email;

public class UpdateUserRequest {

    @Email
    private String email;

    private Boolean active;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
    
    
}
