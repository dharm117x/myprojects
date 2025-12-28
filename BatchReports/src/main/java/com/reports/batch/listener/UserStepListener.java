package com.reports.batch.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class UserStepListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("Step started: " + stepExecution.getStepName());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println(
            "Read=" + stepExecution.getReadCount() +
            ", Write=" + stepExecution.getWriteCount() +
            ", Skip=" + stepExecution.getSkipCount()
        );
        return stepExecution.getExitStatus();
    }
    
    
}
