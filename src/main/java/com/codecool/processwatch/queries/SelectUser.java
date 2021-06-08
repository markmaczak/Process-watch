package com.codecool.processwatch.queries;

import com.codecool.processwatch.domain.Process;
import com.codecool.processwatch.domain.Query;

import java.util.stream.Stream;

public class SelectUser implements Query {
    private String filterUser;

    public void setFilterUser(String filterUser) {
        this.filterUser = filterUser;
    }

    public Stream<Process> run(Stream<Process> input) {
        Stream<Process> filteredName = input.filter(i -> i.getUserName().contains(filterUser));
        return filteredName;
    }


}
