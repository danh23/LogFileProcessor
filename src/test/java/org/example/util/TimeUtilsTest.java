package org.example.util;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeUtilsTest {

    @Test
    void parseTimestamp() {
        LocalTime expected = LocalTime.of(12, 34, 56);
        LocalTime result = TimeUtils.parseTimestamp("12:34:56");
        assertEquals(expected, result);
    }

    @Test
    void parseTimestamp_shouldThrowException() {
        assertThrows(NullPointerException.class, () ->
                TimeUtils.parseTimestamp(null)
        );
    }

    @Test
    void getSecondsFromTime() {
        LocalTime time = LocalTime.of(1, 1, 1);
        int expectedSeconds = 3661; // 1 hour + 1 minute + 1 second
        int result = TimeUtils.getSecondsFromTime(time);
        assertEquals(expectedSeconds, result);
    }

    @Test
    void getSecondsFromTime_midnight() {
        LocalTime time = LocalTime.of(0, 0, 0);
        int expectedSeconds = 0; // midnight
        int result = TimeUtils.getSecondsFromTime(time);
        assertEquals(expectedSeconds, result);
    }

    @Test
    void getSecondsFromTime_shouldThrowException() {
        assertThrows(NullPointerException.class, () ->
                TimeUtils.getSecondsFromTime(null)
        );
    }
}