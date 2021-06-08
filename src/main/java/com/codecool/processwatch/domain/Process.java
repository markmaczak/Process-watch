package com.codecool.processwatch.domain;

import java.util.Arrays;

public class Process {
    private long pid;
    private long parentPid;
    private User user;
    private String name;
    private String[] args;

    public Process(long pid, long parentPid, User user, String name, String[] args) {
        this.pid = pid;
        this.parentPid = parentPid;
        this.user = user;
        this.name = name;
        this.args = args;
    }

    public long getPid() {
        return pid;
    }

    public long getParentPid() {
        return parentPid;
    }

    public String getUserName() {
        return user.getName();
    }

    public String getName() {
        return name;
    }

    public String[] getArgs() {
        return Arrays.copyOf(args, args.length);
    }

    @Override
    public String toString() {
        return String.format("Process {pid = %6d, parentPid = %6d, user = %s}",
                             pid, parentPid, user.getName());
    }

    @Override
    public boolean equals(Object o) {
        var clazz = getClass();
        if (!clazz.isInstance(o)) {
            return false;
        }

        if (this == o) {
            return true;
        }

        var other = clazz.cast(o);

        return this.pid == other.pid;
    }

    @Override
    public int hashCode() {
        var objPid = Long.valueOf(pid);
        return objPid.hashCode();
    }
}
