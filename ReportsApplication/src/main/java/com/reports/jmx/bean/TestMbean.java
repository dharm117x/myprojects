package com.reports.jmx.bean;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "batch:name=TestMbean", description = "Test Mbean")
public class TestMbean {

	private String status = "IDLE";

    @ManagedAttribute
    public String getStatus() {
        return status;
    }

    @ManagedAttribute
    public void setStatus(String status) {
        this.status = status;
    }

	@ManagedOperation(description = "test method with data and id")
	public String testMethod(String data, Long id) {
		System.out.println("TestMbean.testMethod()");
		return id +"--"+data;
	}
}
