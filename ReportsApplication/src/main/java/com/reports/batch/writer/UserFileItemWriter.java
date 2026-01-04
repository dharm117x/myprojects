package com.reports.batch.writer;

import java.time.format.DateTimeFormatter;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import com.reports.batch.dto.UserDto;

@Component
@StepScope
public class UserFileItemWriter extends FlatFileItemWriter<UserDto>  implements StepExecutionListener {
	
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	@Value("#{jobParameters['outputFile']}") 
	String outputFile;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        Long batchNo = stepExecution.getJobExecution().getId();
        setResource(new FileSystemResource("./target/users_" + batchNo +"-"+ outputFile));
    }
    
    
    public UserFileItemWriter() {
//        setResource(new FileSystemResource("./target/"+outputFile));
        setAppendAllowed(false);   // overwrite file
        setHeaderCallback(writer -> writer.write("ID|NAME|EMAIL|BATCHNO|CREATED_DT"));

        setLineAggregator(user ->
                user.getId() + "|" +
                user.getName() + "|" +
                user.getEmail()+ "|" +
                user.getBatchNo() + "|" +
                formatter.format(user.getCreatedDate())
        );
    }
}
