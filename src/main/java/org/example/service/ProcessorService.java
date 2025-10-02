package org.example.service;

import org.example.mapper.TaskMapper;
import org.example.model.Action;
import org.example.model.Task;
import org.example.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ProcessorService {

    private static final Logger log = LoggerFactory.getLogger(ProcessorService.class);

    public void parseFile(InputStream is) throws IOException {
        ConcurrentHashMap<String, Task> taskMap = new ConcurrentHashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    Task task = TaskMapper.mapTask(line);
                    processLine(task, taskMap);
                } catch (Exception e) {
                    log.error("Error processing line: {}", line, e);
                }
            }
        }
    }

    public void processLine(Task task, ConcurrentMap<String, Task> taskMap) {
        if(task.getAction().equals(Action.START)) {
            taskMap.put(task.getPid(), task);
        } else if(task.getAction().equals(Action.END)) {
            Task startTask = taskMap.remove(task.getPid());
            if(startTask != null) {
                reportTask(startTask.getTimestamp(), task.getTimestamp(), task.getPid());
            } else {
                log.error("END action without matching START for PID: {}", task.getPid());
            }
        }
    }

    public void reportTask(LocalTime startTime, LocalTime endTime, String pid) {
        int executionTimeInSeconds = TimeUtils.getSecondsFromTime(endTime) -
                TimeUtils.getSecondsFromTime(startTime);
        String message = String.format("Task with PID: %s executed in %d seconds", pid, executionTimeInSeconds);
        if (executionTimeInSeconds > 10) {
            log.error(message);
        } else if(executionTimeInSeconds > 5) {
            log.warn(message);
        }
    }
}
