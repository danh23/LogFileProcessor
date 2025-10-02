package org.example.service;

import org.example.model.Action;
import org.example.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.event.Level;

import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ProcessorServiceTest {

    ProcessorService processorService;
    LoggingService loggingService;

    @BeforeEach
    void setup() {
        loggingService = mock(LoggingService.class);
        processorService = new ProcessorService(loggingService);
    }

    @Test
    void processLine_shouldStoreTaskByPid() {
        String pid = "1234";
        ConcurrentMap<String, Task> resultMap = new ConcurrentHashMap<>();
        Task task = new Task(LocalTime.of(10, 0, 0), "Process description", Action.START, pid);

        processorService.processLine(task, resultMap);

        assertEquals(1, resultMap.size());
        assertEquals(task, resultMap.get(pid));
    }

    @Test
    void processLine_shouldRemoveMatchingPid() {
        String pid = "1234";
        Task matchingTask = new Task(LocalTime.of(10, 0, 0), "Process description", Action.END, pid);

        ConcurrentMap<String, Task> resultMap = new ConcurrentHashMap<>();
        resultMap.put(pid, matchingTask);
        Task endTask = new Task(LocalTime.of(10, 0, 0), "Process description", Action.END, pid);

        processorService.processLine(endTask, resultMap);

        assertEquals(0, resultMap.size());
    }

    @Test
    void processLine_shouldLogErrorForNotMatchingTask() {
        String pid = "1234";

        ConcurrentMap<String, Task> resultMap = new ConcurrentHashMap<>();
        Task endTask = new Task(LocalTime.of(10, 0, 0), "Process description", Action.END, pid);

        processorService.processLine(endTask, resultMap);

        assertEquals(0, resultMap.size());
        verify(loggingService).log(eq(Level.ERROR), anyString());
    }

    @Test
    void reportTask_shouldLogError() {
        LocalTime startTime = LocalTime.of(10, 0, 0);
        LocalTime endTime = LocalTime.of(11, 0, 0);
        String pid = "1234";
        processorService.reportTask(startTime, endTime, pid);
        verify(loggingService).log(eq(Level.ERROR), anyString());
    }

    @Test
    void reportTask_shouldLogWarning() {
        LocalTime startTime = LocalTime.of(10, 0, 0);
        LocalTime endTime = LocalTime.of(10, 0, 6);
        String pid = "1234";
        processorService.reportTask(startTime, endTime, pid);
        verify(loggingService).log(eq(Level.WARN), anyString());
    }

    @Test
    void reportTask_shouldNotLog() {
        LocalTime startTime = LocalTime.of(10, 0, 0);
        LocalTime endTime = LocalTime.of(10, 0, 2);
        String pid = "1234";
        processorService.reportTask(startTime, endTime, pid);
        verify(loggingService, times(0)).log(any(), anyString());
    }
}