package com.codecool.processwatch.os;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.codecool.processwatch.domain.Process;
import com.codecool.processwatch.domain.ProcessSource;
import com.codecool.processwatch.domain.User;


public class OsProcessSource implements ProcessSource {

    @Override
    public Stream<Process> getProcesses() {
        Stream<ProcessHandle> handle = ProcessHandle.allProcesses();
        List<Process> processes = new ArrayList<>();
        handle.forEach(process -> addProcessed(process, processes));
        //processes.forEach(p -> System.out.println(p));
        return processes.stream();
    }

    public void addProcessed(ProcessHandle process, List<Process> processes) {
        long pid = process.pid();
        long parentPid = process.parent().hashCode();
        //long parentPid = process.parent().get().pid();
        String stringUser = process.info().user().orElse("not available");
        User user = new User(stringUser);
        String name = process.info().command().orElse("not available");
        String[] notAvailable = {"not available"};
        String[] args = process.info().arguments().orElse(notAvailable);

        processes.add(new Process(pid, parentPid, user, name, args));
    }
}
