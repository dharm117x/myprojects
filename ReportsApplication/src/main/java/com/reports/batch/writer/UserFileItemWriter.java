package com.reports.batch.writer;

import java.time.format.DateTimeFormatter;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import com.reports.batch.entity.User;

@Component
@StepScope
public class UserFileItemWriter extends FlatFileItemWriter<User> {
	
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");

    public UserFileItemWriter(
            @Value("#{jobParameters['outputFile']}") String outputFile) {

        setResource(new FileSystemResource(outputFile));
        setAppendAllowed(false);   // overwrite file
        setHeaderCallback(writer -> writer.write("ID|NAME|EMAIL|CREATED_DT"));

        setLineAggregator(user ->
                user.getId() + "|" +
                user.getName() + "|" +
                user.getEmail()+ "|" +
                formatter.format(user.getCreatedDate())
        );
    }
}
