package org.example.mapper;

import org.example.model.Action;
import org.example.model.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskMapperTest {

    @Test
    void mapTask() {
        String input = "12:00:00, Process description, START, 1234";
        Task expected = new Task(
                LocalTime.of(12, 0, 0),
                "Process description",
                Action.START,
                "1234");
        Task result = TaskMapper.mapTask(input);
        assertEquals(expected.getTimestamp(), result.getTimestamp());
        assertEquals(expected.getDescription(), result.getDescription());
        assertEquals(expected.getAction(), result.getAction());
        assertEquals(expected.getPid(), result.getPid());
    }

    @Test
    void parseLine_shouldThrowException() {
        String input = "invalid,line,format";
        assertThrows(IllegalArgumentException.class, () ->
                        TaskMapper.mapTask(input),
                "Invalid line format: " + input
        );
    }
}