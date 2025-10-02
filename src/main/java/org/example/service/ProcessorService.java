package org.example.service;

import org.example.mapper.TaskMapper;
import org.example.model.Action;
import org.example.model.Task;
import org.example.util.TimeUtils;
import org.slf4j.event.Level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ProcessorService {

    private final LoggingService loggingService;

    public ProcessorService(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    //TODO Add unit tests for this class
    //parse the file line by line
    public void parseFile(InputStream is) throws IOException {
        loggingService.log(Level.INFO, "Starting to process the file");

        //TODO The concurrent map was used initially for using threads for parallel processing in case of large files.
        // I left it as is for future scalability.
        ConcurrentHashMap<String, Task> taskMap = new ConcurrentHashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    //parse a line and map to a Task object
                    Task task = TaskMapper.mapTask(line);
                    processLine(task, taskMap);
                } catch (Exception e) {
                    loggingService.log(Level.ERROR, String.format("Error processing line: %s", line), e);
                }
            }
            loggingService.log(Level.INFO, "Finished processing the file");
        }
    }

    //process each line based on action type
    public void processLine(Task task, ConcurrentMap<String, Task> taskMap) {
        if(task.getAction().equals(Action.START)) {
            //if START, store in map
            taskMap.put(task.getPid(), task);
        } else if(task.getAction().equals(Action.END)) {
            //if END, look for matching START by pid
            Task startTask = taskMap.remove(task.getPid());
            if(startTask != null) {
                reportTask(startTask.getTimestamp(), task.getTimestamp(), task.getPid());
            } else {
                loggingService.log(Level.ERROR, String.format("END action without matching START for PID: %s", task.getPid()));
            }
        }
    }

    //report task execution time and log based on thresholds
    public void reportTask(LocalTime startTime, LocalTime endTime, String pid) {
        //calculate execution time in seconds
        int executionTimeInSeconds = TimeUtils.getSecondsFromTime(endTime) -
                TimeUtils.getSecondsFromTime(startTime);
        String message = String.format("Task with PID: %s executed in %d seconds", pid, executionTimeInSeconds);
        //log based on execution time
        if (executionTimeInSeconds > 10) {
            loggingService.log(Level.ERROR, message);
        } else if(executionTimeInSeconds > 5) {
            loggingService.log(Level.WARN, message);
        }
    }
}
