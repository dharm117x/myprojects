package com.reports.jmx.bean;

import java.util.Properties;

import org.springframework.batch.core.launch.JobOperator;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "batch:name=JobOperator",description = "Job Operator")
public class JobOperatorMBean {

    private final JobOperator jobOperator;

    public JobOperatorMBean(JobOperator jobOperator) {
        this.jobOperator = jobOperator;
    }

    @ManagedOperation(description = "Job start..")
    public Long start(String jobName) throws Exception {
    	Properties prop = new Properties();
    	prop.put("run_id", System.currentTimeMillis());
        return jobOperator.start(jobName, prop);
    }
	
	
	@ManagedOperation(description = "Job start with parameters..")
	public Long startWithParams(String jobName, String jobParameters) throws Exception {
	    Properties prop = new Properties();
	    prop.put("run_id", System.currentTimeMillis());
	    if (jobParameters != null && !jobParameters.isEmpty()) {
	        String[] params = jobParameters.split(",");
	        for (String param : params) {
	            String[] keyValue = param.split("=");
	            if (keyValue.length == 2) {
	                prop.put(keyValue[0].trim(), keyValue[1].trim());
	            }
	        }
	    }
	
	    return jobOperator.start(jobName, prop);
	}


    @ManagedOperation(description = "Job stopped")
    public void stop(Long executionId) throws Exception {
        jobOperator.stop(executionId);
    }

    @ManagedOperation(description = "Job re-start..")
    public void restart(Long executionId) throws Exception {
        jobOperator.restart(executionId);
    }
}
