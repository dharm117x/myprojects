package com.reports.batch.reader;

import java.util.Map;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.reports.batch.entity.User;
import com.reports.batch.repository.UserRepository;

@Component
@StepScope
public class UserItemAllDbReader extends RepositoryItemReader<User> {

    public UserItemAllDbReader(UserRepository repository) {

        setRepository(repository);
        setMethodName("findAll");
        setPageSize(500);
        setSort(Map.of("id", Sort.Direction.ASC));
    }
}
