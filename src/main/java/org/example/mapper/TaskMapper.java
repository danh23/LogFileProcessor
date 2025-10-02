package org.example.mapper;

import org.example.model.Task;

public class TaskMapper {

    public static Task mapTask(String line) {
        String[] parts = line.split(",");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid line format: " + line);
        }
        return new Task(
                java.time.LocalTime.parse(parts[0].trim()),
                parts[1].trim(),
                org.example.model.Action.valueOf(parts[2].trim()),
                parts[3].trim()
        );
    }
}
