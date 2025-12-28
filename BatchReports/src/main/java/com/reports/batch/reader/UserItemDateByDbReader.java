package com.reports.batch.reader;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.reports.batch.entity.User;
import com.reports.batch.repository.UserRepository;

@Component
@StepScope //Allows access to job parameters
public class UserItemDateByDbReader extends RepositoryItemReader<User> {

    public UserItemDateByDbReader(UserRepository repo,
        @Value("#{jobParameters['fromDate']}") String fromDate,
        @Value("#{jobParameters['toDate']}") String toDate) {

        setRepository(repo);
        setMethodName("findByCreatedDateBetween");
        setArguments(List.of(
            LocalDate.parse(fromDate),
            LocalDate.parse(toDate)
        ));
        setPageSize(500);
        setSort(Map.of("id", Sort.Direction.ASC));
    }
}
