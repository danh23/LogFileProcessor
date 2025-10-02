package org.example.model;

import java.time.LocalTime;
import java.util.Objects;

public class Task {

    private LocalTime timestamp;
    private String description;
    private Action action;
    private String pid;

    public Task(LocalTime timestamp, String description, Action action, String pid) {
        this.timestamp = timestamp;
        this.description = description;
        this.action = action;
        this.pid = pid;
    }

    public LocalTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(timestamp, task.timestamp) && Objects.equals(description, task.description) && action == task.action && Objects.equals(pid, task.pid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, description, action, pid);
    }
}
